package com.keumbang.resource.controller.dto.request;

import jakarta.validation.constraints.NotNull;

import com.keumbang.resource.entity.enums.OrderStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UpdateOrderStatusRequest", description = "주문 상태 변경 요청 DTO")
public record UpdateOrderStatusRequest(
    @NotNull(message = "주문 변경 상태는 필수 입력 값입니다.")
        @Schema(
            description = "변경하고 싶은 주문 상태",
            example =
                "BUY(구매) - DEPOSITED(입금 완료), RECEIVED(수령 완료), CANCELED(취소)"
                    + ", SELL(판매) - TRANSFERRED(송금 완료), DELIVERED(발송 완료), CANCELED(취소)")
        OrderStatus orderStatus) {}
