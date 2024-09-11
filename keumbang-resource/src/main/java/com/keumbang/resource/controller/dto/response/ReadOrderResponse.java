package com.keumbang.resource.controller.dto.response;

import java.math.BigDecimal;

import com.keumbang.resource.entity.Order;
import com.keumbang.resource.entity.Product;
import com.keumbang.resource.entity.enums.ProductType;
import com.keumbang.resource.entity.enums.PurityType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ReadOrderResponse", description = "주문 상세보기 응답 DTO")
public record ReadOrderResponse(
    @Schema(description = "주문 ID", example = "ORD-20240911213322-SELL-0002-0001")
        String orderNumber,
    @Schema(description = "주문한 상품 ID", example = "1") Long productId,
    @Schema(description = "상품 순도", example = "GOLD_999") PurityType purityType,
    @Schema(description = "상품 1g당 가격", example = "100000") long gramPerPrice,
    @Schema(description = "상품 이름", example = "금 판매합니다") String productName,
    @Schema(description = "상품 설명", example = "99.9% 순도의 금을 판매합니다") String productDescription,
    @Schema(description = "상품 타입", example = "판매용/매입용") ProductType productType,
    @Schema(description = "주문 수량", example = "100.00") BigDecimal quantity,
    @Schema(description = "주문 가격", example = "10000000") BigDecimal orderPrice,
    @Schema(description = "배송 정보", example = "서울시 용산구 123-123") String deliverInfo) {
  public static ReadOrderResponse of(Order order, Product product) {
    return new ReadOrderResponse(
        order.getOrderId(),
        product.getProductId(),
        product.getPurityType(),
        product.getGramPerPrice(),
        product.getProductName(),
        product.getProductDescription(),
        product.getProductType(),
        order.getOrderQuantity(),
        order.getOrderPrice(),
        order.getDeliveryAddressInfo());
  }
}
