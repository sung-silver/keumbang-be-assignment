package com.keumbang.resource.entity.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStatus {
  ORDERED("주문 완료"),
  DEPOSITED("입금 완료"),
  DELIVERED("발송 완료"),
  TRANSFERRED("송금 완료"),
  RECEIVED("수령 완료"),
  CANCELED("주문 취소");

  private final String description;
}
