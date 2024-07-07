package com.rajan.foodDeliveryApp.services.impl;

import com.rajan.foodDeliveryApp.domain.entities.OrderDetailEntity;
import com.rajan.foodDeliveryApp.domain.entities.OrderEntity;
import com.rajan.foodDeliveryApp.repositories.OrderDetailRepository;
import com.rajan.foodDeliveryApp.services.OrderDetailService;
import com.rajan.foodDeliveryApp.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public OrderDetailEntity save(OrderDetailEntity orderDetailEntity) {
        return orderDetailRepository.save(orderDetailEntity);
    }

    @Override
    public Page<OrderDetailEntity> findALl(Pageable pageable, Long orderId) {
        return null;
    }
}
