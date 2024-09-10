package com.keumbang.resource.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductType {
  SELL("판매용"),
  PURCHASE("매입용");

  private final String description;
}
