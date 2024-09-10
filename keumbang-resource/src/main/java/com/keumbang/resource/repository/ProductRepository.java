package com.keumbang.resource.repository;

import static com.keumbang.resource.exception.exceptionType.ProductExceptionType.*;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.keumbang.resource.entity.Product;
import com.keumbang.resource.exception.CustomException;

public interface ProductRepository extends JpaRepository<Product, Long> {
  Product findByProductId(Long productId);

  default Product findByProductIdThrow(Long productId) {
    return Optional.ofNullable(findByProductId(productId))
        .orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));
  }
}
