package com.keumbang.resource.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keumbang.resource.controller.dto.response.ReadOrderResponse;
import com.keumbang.resource.entity.Order;
import com.keumbang.resource.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadOrderService {
  private final OrderRepository orderRepository;
  private final MemberService memberService;

  public ReadOrderResponse getOrder(final String orderId) {
    Long memberId = memberService.getMemberId();
    Order order = orderRepository.findByOrderIdAndCustomerId(orderId, memberId);
    return ReadOrderResponse.of(order, order.getProduct());
  }
}
