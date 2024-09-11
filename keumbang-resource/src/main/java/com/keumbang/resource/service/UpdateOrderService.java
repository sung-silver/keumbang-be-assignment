package com.keumbang.resource.service;

import static com.keumbang.resource.exception.exceptionType.OrderExceptionType.*;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keumbang.resource.controller.dto.request.UpdateOrderStatusRequest;
import com.keumbang.resource.controller.dto.response.UpdateOrderStatusResponse;
import com.keumbang.resource.entity.Order;
import com.keumbang.resource.entity.enums.OrderStatus;
import com.keumbang.resource.entity.enums.OrderType;
import com.keumbang.resource.exception.CustomException;
import com.keumbang.resource.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateOrderService {
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

  private final OrderRepository orderRepository;
  private final MemberService memberService;

  @Transactional
  public UpdateOrderStatusResponse updateBuyOrderStatus(
      final String orderId, final UpdateOrderStatusRequest request) {
    long memberId = memberService.getMemberId();
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

  @Transactional
  public UpdateOrderStatusResponse updateSellOrderStatus(
      final String orderId, final UpdateOrderStatusRequest request) {
    long memberId = memberService.getMemberId();
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
}
