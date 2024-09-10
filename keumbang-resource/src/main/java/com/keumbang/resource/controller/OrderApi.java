package com.keumbang.resource.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.keumbang.resource.common.response.ErrorResponse;
import com.keumbang.resource.common.response.SuccessResponse;
import com.keumbang.resource.controller.dto.request.CreateOrderRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "order", description = "주문 관련 API")
public interface OrderApi {
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "주문에 성공했을 경우"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않은 상품에 대해 주문을 요청하는 경우",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  @Operation(summary = "주문 생성 API", description = "사용자가 상품을 주문합니다")
  ResponseEntity<SuccessResponse<Void>> createOrder(
      @RequestBody @Valid final CreateOrderRequest request);
}
