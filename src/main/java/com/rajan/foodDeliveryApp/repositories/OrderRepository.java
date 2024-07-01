package com.rajan.foodDeliveryApp.repositories;

import com.rajan.foodDeliveryApp.domain.dto.OrderDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderDto, Long> {
}
