package com.keumbang.resource.repository;

import static com.keumbang.resource.exception.exceptionType.OrderExceptionType.*;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.keumbang.resource.entity.Order;
import com.keumbang.resource.entity.enums.OrderType;
import com.keumbang.resource.exception.CustomException;

public interface OrderRepository extends JpaRepository<Order, String> {
  Order findByOrderIdAndCustomerIdAndOrderType(
      final String orderId, final Long customerId, final OrderType orderType);

  default Order findByOrderIdAndCustomerIdAndOrderTypeOrThrow(
      final String orderId, final Long customerId, final OrderType orderType) {
    return Optional.ofNullable(
            findByOrderIdAndCustomerIdAndOrderType(orderId, customerId, orderType))
        .orElseThrow(() -> new CustomException(INVALID_UPDATE_ORDER));
  }

  Order findByOrderIdAndCustomerId(final String orderId, final Long memberId);

  default Order findByOrderIdAndCustomerIdOrThrow(final String orderId, final Long memberId) {
    return Optional.ofNullable(findByOrderIdAndCustomerId(orderId, memberId))
        .orElseThrow(() -> new CustomException(NOT_FOUND_ORDER));
  }
}
