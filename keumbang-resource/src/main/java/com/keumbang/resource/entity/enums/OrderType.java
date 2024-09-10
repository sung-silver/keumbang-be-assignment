package com.keumbang.resource.entity.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderType {
  BUY("구매"),
  SELL("판매");

  private final String description;
}
