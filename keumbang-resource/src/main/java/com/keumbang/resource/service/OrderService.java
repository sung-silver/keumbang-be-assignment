package com.keumbang.resource.service;

import static com.keumbang.resource.exception.exceptionType.OrderExceptionType.*;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keumbang.resource.controller.dto.request.CreateOrderRequest;
import com.keumbang.resource.controller.dto.request.UpdateOrderStatusRequest;
import com.keumbang.resource.controller.dto.response.UpdateOrderStatusResponse;
import com.keumbang.resource.entity.Order;
import com.keumbang.resource.entity.Product;
import com.keumbang.resource.entity.enums.OrderStatus;
import com.keumbang.resource.entity.enums.OrderType;
import com.keumbang.resource.entity.enums.ProductType;
import com.keumbang.resource.exception.CustomException;
import com.keumbang.resource.repository.OrderRepository;
import com.keumbang.resource.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private static final int NEXT_ORDER = 1;

  private static final ConcurrentHashMap<OrderStatus, Integer> buyOrderStatusMap =
      new ConcurrentHashMap<>();
  private static final ConcurrentHashMap<OrderStatus, Integer> sellOrderStatusMap =
      new ConcurrentHashMap<>();

  static {
    buyOrderStatusMap.put(OrderStatus.CANCELED, 0);
    buyOrderStatusMap.put(OrderStatus.ORDERED, 1);
    buyOrderStatusMap.put(OrderStatus.TRANSFERRED, 2);
    buyOrderStatusMap.put(OrderStatus.RECEIVED, 3);

    sellOrderStatusMap.put(OrderStatus.CANCELED, 0);
    sellOrderStatusMap.put(OrderStatus.ORDERED, 1);
    sellOrderStatusMap.put(OrderStatus.DEPOSITED, 2);
    sellOrderStatusMap.put(OrderStatus.DELIVERED, 3);
  }

  @Transactional
  public String createOrder(final CreateOrderRequest request) {
    // TODO: 로그인한 사용자의 ID를 가져오는 로직 구현
    Long memberId = 1L;
    Product product = productRepository.findByProductIdThrow(request.productId());
    validateOrderType(product.getProductType(), request.orderType());
    BigDecimal orderPrice = getOrderPrice(product.getGramPerPrice(), request.orderQuantity());
    Order order =
        Order.builder()
            .customerId(memberId)
            .product(product)
            .orderType(request.orderType())
            .orderQuantity(request.orderQuantity())
            .orderPrice(orderPrice)
            .deliveryAddressInfo(request.userAddressInfo())
            .build();

    return orderRepository.save(order).getOrderId();
  }

  private BigDecimal getOrderPrice(final long gramPerPrice, final BigDecimal orderQuantity) {
    BigDecimal price = BigDecimal.valueOf(gramPerPrice).multiply(orderQuantity);
    return price;
  }

  private void validateOrderType(final ProductType productType, final OrderType orderType) {
    boolean isInvalidOrder =
        productType.equals(ProductType.SELL) && orderType.equals(OrderType.SELL)
            || productType.equals(ProductType.PURCHASE) && orderType.equals(OrderType.BUY);
    if (isInvalidOrder) {
      throw new CustomException(INVALID_ORDER_TYPE);
    }
  }

  public UpdateOrderStatusResponse updateBuyOrderStatus(
      final String orderId, final UpdateOrderStatusRequest request) {
    // TODO:: 로그인한 사용자의 ID를 가져오는 로직 구현
    Long memberId = 1L;
    Order order =
        orderRepository.findByOrderIdAndCustomerIdAndOrderTypeOrThrow(
            orderId, memberId, OrderType.BUY);
    validateBuyOrderStatus(order.getOrderStatus(), request.orderStatus());
    order.updateOrderStatus(request.orderStatus());
    return UpdateOrderStatusResponse.of(order.getOrderId(), order.getOrderStatus());
  }

  private void validateBuyOrderStatus(
      final OrderStatus currentOrderStatus, final OrderStatus requestOrderStatus) {
    if (isInvalidOrderStatus(requestOrderStatus, buyOrderStatusMap)) {
      throw new CustomException(INVALID_UPDATE_ORDER);
    }

    switch (requestOrderStatus) {
      case CANCELED:
        if (isAlreadyCanceled(currentOrderStatus)
            || isInvalidCancelOrder(currentOrderStatus, requestOrderStatus)) {
          throw new CustomException(INVALID_UPDATE_ORDER);
        }
        break;
      default:
        if (isInvalidOrdering(currentOrderStatus, requestOrderStatus, buyOrderStatusMap)) {
          throw new CustomException(INVALID_UPDATE_ORDER);
        }
    }
  }

  public UpdateOrderStatusResponse updateSellOrderStatus(
      final String orderId, final UpdateOrderStatusRequest request) {
    // TODO:: 로그인한 사용자의 ID를 가져오는 로직 구현
    Long memberId = 1L;
    Order order =
        orderRepository.findByOrderIdAndCustomerIdAndOrderTypeOrThrow(
            orderId, memberId, OrderType.SELL);
    validateSellOrderStatus(order.getOrderStatus(), request.orderStatus());
    order.updateOrderStatus(request.orderStatus());
    return UpdateOrderStatusResponse.of(order.getOrderId(), order.getOrderStatus());
  }

  private void validateSellOrderStatus(
      final OrderStatus currentOrderStatus, final OrderStatus requestOrderStatus) {
    if (isInvalidOrderStatus(requestOrderStatus, sellOrderStatusMap)) {
      throw new CustomException(INVALID_UPDATE_ORDER);
    }

    switch (requestOrderStatus) {
      case CANCELED:
        if (isAlreadyCanceled(currentOrderStatus)
            || isInvalidCancelOrder(currentOrderStatus, requestOrderStatus)) {
          throw new CustomException(INVALID_UPDATE_ORDER);
        }
        break;
      default:
        if (isInvalidOrdering(currentOrderStatus, requestOrderStatus, sellOrderStatusMap)) {
          throw new CustomException(INVALID_UPDATE_ORDER);
        }
    }
  }

  private boolean isInvalidOrderStatus(
      final OrderStatus requestOrderStatus,
      final ConcurrentHashMap<OrderStatus, Integer> orderStatusMap) {
    return orderStatusMap.get(requestOrderStatus) == null;
  }

  private boolean isAlreadyCanceled(final OrderStatus currentOrderStatus) {
    return currentOrderStatus.equals(OrderStatus.CANCELED);
  }

  private boolean isInvalidCancelOrder(
      final OrderStatus currentOrderStatus, final OrderStatus requestOrderStatus) {
    return (currentOrderStatus.equals(OrderStatus.DELIVERED)
            || currentOrderStatus.equals(OrderStatus.RECEIVED))
        && requestOrderStatus.equals(OrderStatus.CANCELED);
  }

  private boolean isInvalidOrdering(
      final OrderStatus currentOrderStatus,
      final OrderStatus requestOrderStatus,
      final ConcurrentHashMap<OrderStatus, Integer> orderStatusMap) {
    return orderStatusMap.get(currentOrderStatus) + NEXT_ORDER
        != orderStatusMap.get(requestOrderStatus);
  }

  public void deleteOrder(final String orderId) {
    // TODO: memberId 가져오는 로직 필요
    Long memberId = 1L;
    Order order = orderRepository.findByOrderIdAndCustomerIdOrThrow(orderId, memberId);
    validateDeleteOrder(order);
    orderRepository.delete(order);
  }

  private void validateDeleteOrder(final Order order) {
    boolean isInvalidDeleteOrder =
        order.getOrderStatus().equals(OrderStatus.RECEIVED)
            || order.getOrderStatus().equals(OrderStatus.DELIVERED);
    if (isInvalidDeleteOrder) {
      throw new CustomException(INVALID_DELETE_ORDER);
    }
  }
}
