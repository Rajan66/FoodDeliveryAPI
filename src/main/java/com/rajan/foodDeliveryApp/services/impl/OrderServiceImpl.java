package com.rajan.foodDeliveryApp.services.impl;

import com.rajan.foodDeliveryApp.domain.entities.OrderEntity;
import com.rajan.foodDeliveryApp.domain.entities.RestaurantEntity;
import com.rajan.foodDeliveryApp.domain.entities.UserEntity;
import com.rajan.foodDeliveryApp.repositories.OrderRepository;
import com.rajan.foodDeliveryApp.services.OrderService;
import com.rajan.foodDeliveryApp.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final RestaurantService restaurantService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, RestaurantService restaurantService) {
        this.orderRepository = orderRepository;
        this.restaurantService = restaurantService;
    }

    @Override
    public OrderEntity save(OrderEntity orderEntity, UserEntity user, RestaurantEntity restaurant) {
        orderEntity.setUser(user);
        orderEntity.setRestaurant(restaurant);
        return orderRepository.save(orderEntity);
    }

    @Override
    public OrderEntity save(OrderEntity orderEntity) {
        return orderRepository.save(orderEntity);
    }

    @Override
    public Page<OrderEntity> findAll(Pageable pageable, Long userId) {
        return orderRepository.findAllByUserId(pageable, userId);
    }

    @Override
    public Optional<OrderEntity> findOne(Long id) {
        return orderRepository.findById(id);
    }
}
