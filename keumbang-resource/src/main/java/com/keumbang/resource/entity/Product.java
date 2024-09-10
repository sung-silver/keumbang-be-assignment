package com.keumbang.resource.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import com.keumbang.resource.entity.enums.ProductType;
import com.keumbang.resource.entity.enums.PurityType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long productId;

  @Enumerated(EnumType.STRING)
  @NotNull
  private PurityType purityType;

  private long price;

  @NotNull private String productName;

  @NotNull private String productDescription;

  @Enumerated(EnumType.STRING)
  @NotNull
  private ProductType productType;
}
