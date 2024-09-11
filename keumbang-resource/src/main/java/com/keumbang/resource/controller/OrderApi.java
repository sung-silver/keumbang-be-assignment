package com.keumbang.resource.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.keumbang.resource.common.response.ErrorResponse;
import com.keumbang.resource.common.response.SuccessResponse;
import com.keumbang.resource.controller.dto.request.CreateOrderRequest;
import com.keumbang.resource.controller.dto.request.UpdateOrderStatusRequest;
import com.keumbang.resource.controller.dto.response.UpdateOrderStatusResponse;

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
            responseCode = "400",
            description = "판매 상품에 대해 판매 주문을 하는 경우 또는 매입 상품에 대해 구매 주문을 하는 경우",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않은 상품에 대해 주문을 요청하는 경우",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  @Operation(summary = "주문 생성 API", description = "사용자가 상품을 주문합니다")
  ResponseEntity<SuccessResponse<Void>> createOrder(
      @RequestBody @Valid final CreateOrderRequest request);

  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "구매 주문 상태 변경에 성공했을 경우"),
        @ApiResponse(
            responseCode = "400",
            description =
                "구매 주문에 대해 상태를 변경할 수 없는 경우(1. 구매 주문 상태 순서: 주문 완료 -> 송금 완료 -> 수령 완료, 각 순서가 이미 진행되었을 때에는 이전 순서로 돌아갈 수 없다"
                    + "2. 사용자가 구매 주문한 내역이 아닌 경우"
                    + "3. 수령 완료 후에 구매 주문을 취소하는 경우",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      })
  @Operation(summary = "구매 주문 상태 변경 API", description = "사용자가 상품을 주문합니다")
  ResponseEntity<SuccessResponse<UpdateOrderStatusResponse>> updateBuyOrder(
      @PathVariable("orderId") final String orderId,
      @RequestBody @Valid final UpdateOrderStatusRequest request);

  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "판매 주문 상태 변경에 성공했을 경우"),
        @ApiResponse(
            responseCode = "400",
            description =
                "판매 주문에 대해 상태를 변경할 수 없는 경우(1. 판매 주문 상태 순서: 주문 완료 -> 입금 완료 -> 발송 완료, 각 순서가 이미 진행되었을 때에는 이전 순서로 돌아갈 수 없다"
                    + "2. 사용자가 판매 주문한 내역이 아닌 경우"
                    + "3. 발송 완료 후에 판매 주문을 취소하는 경우",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      })
  @Operation(summary = "판매 주문 상태 변경 API", description = "사용자가 상품을 주문합니다")
  ResponseEntity<SuccessResponse<UpdateOrderStatusResponse>> updateSellOrder(
      @PathVariable("orderId") final String orderId,
      @RequestBody @Valid final UpdateOrderStatusRequest request);

  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "판매 주문 상태 변경에 성공했을 경우"),
        @ApiResponse(
            responseCode = "400",
            description = "주문 취소 상태가 아닌 경우에 주문 삭제 요청하는 경우, 사용자가 주문한 내역을 찾을 수 없는 경우",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  @Operation(summary = "판매 주문 상태 변경 API", description = "사용자가 상품을 주문을 취소합니다")
  ResponseEntity<SuccessResponse<Void>> deleteOrder(@PathVariable("orderId") final String orderId);
}
