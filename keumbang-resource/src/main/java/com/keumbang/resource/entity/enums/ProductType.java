package com.keumbang.resource.entity.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProductType {
  SELL("판매용"),
  PURCHASE("매입용");

  private final String description;
}
