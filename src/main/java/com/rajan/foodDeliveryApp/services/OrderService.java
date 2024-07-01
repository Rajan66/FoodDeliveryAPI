package com.rajan.foodDeliveryApp.services;

import com.rajan.foodDeliveryApp.domain.entities.OrderEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public interface OrderService {
    OrderEntity save(OrderEntity orderEntity);

    Page<OrderEntity> findAll(Pageable pageable, Long userId);
}
