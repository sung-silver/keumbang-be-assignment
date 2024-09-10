package com.keumbang.resource.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.keumbang.resource.entity.Order;

public interface OrderRepository extends JpaRepository<Order, String> {}
