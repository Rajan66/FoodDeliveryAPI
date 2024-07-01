package com.rajan.foodDeliveryApp.services.impl;

import com.rajan.foodDeliveryApp.domain.entities.OrderDetailEntity;
import com.rajan.foodDeliveryApp.services.OrderDetailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class OrderDetailServiceImpl implements OrderDetailService {

    // TODO create repository and implement methods
    @Override
    public OrderDetailEntity save(OrderDetailEntity orderDetailEntity) {
        return null;
    }

    @Override
    public Page<OrderDetailEntity> findALl(Pageable pageable, Long orderId) {
        return null;
    }
}
