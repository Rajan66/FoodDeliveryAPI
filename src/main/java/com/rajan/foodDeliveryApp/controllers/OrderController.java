package com.rajan.foodDeliveryApp.controllers;

import com.rajan.foodDeliveryApp.domain.dto.OrderDetailDto;
import com.rajan.foodDeliveryApp.domain.dto.OrderDto;
import com.rajan.foodDeliveryApp.domain.entities.OrderDetailEntity;
import com.rajan.foodDeliveryApp.domain.entities.OrderEntity;
import com.rajan.foodDeliveryApp.domain.entities.RestaurantEntity;
import com.rajan.foodDeliveryApp.domain.entities.UserEntity;
import com.rajan.foodDeliveryApp.mappers.Mapper;
import com.rajan.foodDeliveryApp.services.OrderDetailService;
import com.rajan.foodDeliveryApp.services.OrderService;
import com.rajan.foodDeliveryApp.services.RestaurantService;
import com.rajan.foodDeliveryApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final OrderDetailService orderDetailService;

    private final Mapper<OrderEntity, OrderDto> orderMapper;
    private final Mapper<OrderDetailEntity, OrderDetailDto> orderDetailMapper;

    @Autowired
    public OrderController(
            OrderService orderService,
            UserService userService,
            RestaurantService restaurantService,
            OrderDetailService orderDetailService,
            Mapper<OrderEntity, OrderDto> orderMapper,
            Mapper<OrderDetailEntity, OrderDetailDto> orderDetailMapper
    ) {
        this.orderService = orderService;
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.orderDetailService = orderDetailService;
        this.orderMapper = orderMapper;
        this.orderDetailMapper = orderDetailMapper;
    }


    @GetMapping("/users/{id}/orders/1")
    public String hello() {
        return "hello world";
    }

    @GetMapping("/{id}/orders")
    public Page<OrderDto> listOrders(@PathVariable("id") Long id, Pageable pageable) {
        Page<OrderEntity> ordersListEntity = orderService.findAll(pageable, id);
        return ordersListEntity.map(orderMapper::mapTo);
    }

    @PostMapping("/{id}/orders")
    public ResponseEntity<OrderDto> createOrder(@PathVariable("id") Long id, @RequestBody OrderDto orderDto) {
        UserEntity orderUser = userService
                .findOne(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        RestaurantEntity orderRestaurant = restaurantService
                .findOne(orderDto.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        OrderEntity orderEntity = orderMapper.mapFrom(orderDto);
        orderEntity.setUser(orderUser);
        orderEntity.setRestaurant(orderRestaurant);

        OrderEntity savedOrderEntity = orderService.save(orderEntity);

        OrderEntity finalSavedOrderEntity = savedOrderEntity;
        List<OrderDetailEntity> orderDetails = orderDto.getOrderDetails()
                .stream()
                .map(orderDetailDto -> {
                    OrderDetailEntity newOrderDetail = orderDetailMapper.mapFrom(orderDetailDto);
                    newOrderDetail.setOrder((finalSavedOrderEntity));
                    return orderDetailService.save(newOrderDetail);
                }).collect(Collectors.toList());

        savedOrderEntity.setOrderDetails(orderDetails);
        savedOrderEntity = orderService.save(savedOrderEntity);

        OrderDto savedOrderDto = orderMapper.mapTo(savedOrderEntity);

        return new ResponseEntity<>(savedOrderDto, HttpStatus.CREATED);
    }
}
