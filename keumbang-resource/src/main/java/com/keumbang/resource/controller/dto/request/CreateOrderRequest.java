package com.keumbang.resource.controller.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import com.keumbang.resource.entity.enums.OrderType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CreateOrderRequest", description = "주문 생성 요청 DTO")
public record CreateOrderRequest(
    @NotNull(message = "상품 ID는 필수 입력 값입니다.") @Schema(description = "상품 ID", example = "1")
        Long productId,
    @NotNull(message = "주문 타입은 필수 입력 값입니다.")
        @Schema(description = "주문 유형", example = "BUY(구매), SELL(판매)")
        OrderType orderType,
    @NotNull(message = "주문 수량은 필수 입력 값입니다.")
        @NotNull(message = "주문 수량은 필수 입력 값입니다.")
        @Digits(integer = 10, fraction = 2, message = "주문 수량은 최대 10자리 숫자와 소수점 둘 째 자리까지 입력할 수 있습니다.")
        @Schema(description = "주문 수량", example = "주문 수량은 g 단위이며 최대 10자리 숫자와 소수점 둘 째 자리까지 입력할 수 있음")
        BigDecimal orderQuantity,
    @NotNull(message = "주소는 필수 입력 값입니다.")
        @Schema(description = "사용자 주소", example = "배송 받거나 환불 받을 주소지를 입력")
        String userAddressInfo) {}
