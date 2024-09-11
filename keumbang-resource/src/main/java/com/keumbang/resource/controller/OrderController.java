package com.keumbang.resource.controller;

import static com.keumbang.resource.exception.exceptionType.OrderSuccessType.*;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keumbang.resource.common.response.SuccessResponse;
import com.keumbang.resource.controller.dto.request.CreateOrderRequest;
import com.keumbang.resource.controller.dto.request.UpdateOrderStatusRequest;
import com.keumbang.resource.controller.dto.response.UpdateOrderStatusResponse;
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

  @Override
  @PatchMapping("/buy/{orderId}/status")
  public ResponseEntity<SuccessResponse<UpdateOrderStatusResponse>> updateBuyOrder(
      @PathVariable("orderId") final String orderId,
      @RequestBody @Valid final UpdateOrderStatusRequest request) {
    UpdateOrderStatusResponse response = orderService.updateBuyOrderStatus(orderId, request);
    return ResponseEntity.ok(SuccessResponse.of(UPDATE_ORDER_STATUS_SUCCESS, response));
  }

  @Override
  @PatchMapping("/sell/{orderId}/status")
  public ResponseEntity<SuccessResponse<UpdateOrderStatusResponse>> updateSellOrder(
      @PathVariable("orderId") final String orderId,
      @RequestBody @Valid final UpdateOrderStatusRequest request) {
    UpdateOrderStatusResponse response = orderService.updateSellOrderStatus(orderId, request);
    return ResponseEntity.ok(SuccessResponse.of(UPDATE_ORDER_STATUS_SUCCESS, response));
  }

  @Override
  @DeleteMapping("/{orderId}")
  public ResponseEntity<SuccessResponse<Void>> deleteOrder(
      @PathVariable("orderId") final String orderId) {
    orderService.deleteOrder(orderId);
    return ResponseEntity.noContent().build();
  }
}
