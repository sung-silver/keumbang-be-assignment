package com.keumbang.resource.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.keumbang.resource.entity.enums.OrderStatus;
import com.keumbang.resource.entity.enums.OrderType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE order SET is_deleted = true, deleted_at = now() WHERE order_id = ?")
@SQLRestriction("is_deleted = false")
@Getter
public class Order extends BaseTimeEntity {
  @Id private String orderId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @NotNull private Long customerId;

  @NotNull
  @Enumerated(EnumType.STRING)
  private OrderType orderType;

  @Column(precision = 10, scale = 2)
  private BigDecimal orderQuantity;

  private long orderPrice;

  @NotNull
  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  private String deliveryAddressInfo;

  private boolean isDeleted;

  private LocalDateTime deletedAt;

  @Builder
  private Order(
      Product product,
      Long customerId,
      OrderType orderType,
      BigDecimal orderQuantity,
      long orderPrice,
      String deliveryAddressInfo) {
    this.orderId = createOrderId(customerId, product.getProductId(), orderType);
    this.product = product;
    this.customerId = customerId;
    this.orderType = orderType;
    this.orderQuantity = orderQuantity;
    this.orderPrice = orderPrice;
    this.orderStatus = OrderStatus.ORDERED;
    this.deliveryAddressInfo = deliveryAddressInfo;
  }

  private String createOrderId(Long customerId, Long productId, OrderType orderType) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    String dateTime = LocalDateTime.now().format(dateFormatter);

    String formattedProductId = String.format("%08d", productId);
    String formattedCustomerId = String.format("%08d", customerId);

    return String.format(
        "ORD-%s-%s-%s-%s", dateTime, orderType, formattedProductId, formattedCustomerId);
  }
}
