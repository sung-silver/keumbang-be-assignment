package com.keumbang.resource.service;

import static com.keumbang.resource.exception.exceptionType.OrderExceptionType.*;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keumbang.resource.controller.dto.request.CreateOrderRequest;
import com.keumbang.resource.entity.Order;
import com.keumbang.resource.entity.Product;
import com.keumbang.resource.entity.enums.OrderType;
import com.keumbang.resource.entity.enums.ProductType;
import com.keumbang.resource.exception.CustomException;
import com.keumbang.resource.repository.OrderRepository;
import com.keumbang.resource.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CreateOrderService {
  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final MemberService memberService;

  @Transactional
  public String createOrder(final CreateOrderRequest request) {
    long memberId = memberService.getMemberId();
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
}
