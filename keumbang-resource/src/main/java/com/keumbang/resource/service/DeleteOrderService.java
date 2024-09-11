package com.keumbang.resource.service;

import static com.keumbang.resource.exception.exceptionType.OrderExceptionType.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keumbang.resource.entity.Order;
import com.keumbang.resource.entity.enums.OrderStatus;
import com.keumbang.resource.exception.CustomException;
import com.keumbang.resource.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteOrderService {
  private final OrderRepository orderRepository;
  private final MemberService memberService;

  @Transactional
  public void deleteOrder(final String orderId) {
    long memberId = memberService.getMemberId();
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
