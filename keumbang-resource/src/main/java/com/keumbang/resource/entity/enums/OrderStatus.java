package com.keumbang.resource.entity.enums;

import java.util.HashMap;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
  ORDERED("주문 완료"),
  DEPOSITED("입금 완료"),
  DELIVERED("발송 완료"),
  TRANSFERRED("송금 완료"),
  RECEIVED("수령 완료"),
  CANCELED("주문 취소");

  private final String description;

  public static HashMap<OrderStatus, Integer> getBuyOrderStatusMap() {
    HashMap<OrderStatus, Integer> map = new HashMap<>();
    map.put(OrderStatus.ORDERED, 1);
    map.put(OrderStatus.TRANSFERRED, 2);
    map.put(OrderStatus.RECEIVED, 3);
    return map;
  }

  public static HashMap<OrderStatus, Integer> getSellOrderStatusMap() {
    HashMap<OrderStatus, Integer> map = new HashMap<>();
    map.put(OrderStatus.ORDERED, 1);
    map.put(OrderStatus.DEPOSITED, 2);
    map.put(OrderStatus.DELIVERED, 3);
    return map;
  }
}
