package com.keumbang.resource.entity.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PurityType {
  GOLD_999("99.9"),
  GOLD_9999("99.99");

  private final String description;
}
