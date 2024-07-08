package com.rajan.foodDeliveryApp.services;

import com.rajan.foodDeliveryApp.domain.entities.OrderDetailEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OrderDetailService {
    OrderDetailEntity save(OrderDetailEntity orderDetailEntity);

    Page<OrderDetailEntity> findALl(Pageable pageable, Long orderId);
}
