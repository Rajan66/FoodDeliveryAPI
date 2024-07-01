package com.rajan.foodDeliveryApp.services.impl;

import com.rajan.foodDeliveryApp.domain.entities.OrderEntity;
import com.rajan.foodDeliveryApp.services.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    // TODO create repository and implement methods

    @Override
    public OrderEntity save(OrderEntity orderEntity) {
        return null;
    }

    @Override
    public Page<OrderEntity> findAll(Pageable pageable, Long userId) {
        return null;
    }
}
