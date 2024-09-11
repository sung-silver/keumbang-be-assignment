package com.keumbang.resource.controller.dto.response;

import com.keumbang.resource.entity.enums.OrderStatus;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateOrderStatusResponse(
	@Schema(description = "상태를 변경한 주문 ID", example = "1")
	String orderId,
	@Schema(description = "주문의 변경된 상태", example = "CANCELED")
	OrderStatus currentOrderStatus) {}
