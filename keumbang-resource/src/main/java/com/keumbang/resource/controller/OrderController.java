package com.keumbang.resource.controller;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keumbang.resource.common.response.SuccessResponse;
import com.keumbang.resource.controller.dto.request.CreateOrderRequest;
import com.keumbang.resource.exception.exceptionType.OrderSuccessType;
import com.keumbang.resource.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController implements OrderApi {
  private static final String ORDER_URL = "/orders/";
  private final OrderService orderService;

  @Override
  @PostMapping
  public ResponseEntity<SuccessResponse<Void>> createOrder(
      @RequestBody @Valid final CreateOrderRequest request) {
    String orderId = orderService.createOrder(request);
    URI uri = URI.create(ORDER_URL + orderId);
    return ResponseEntity.created(uri)
        .body(SuccessResponse.of(OrderSuccessType.CREATE_ORDER_SUCCESS));
  }
}
