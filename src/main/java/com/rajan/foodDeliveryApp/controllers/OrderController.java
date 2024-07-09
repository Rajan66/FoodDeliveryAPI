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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
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


    @GetMapping("/{id}/orders")
    public Page<OrderDto> listOrders(@PathVariable("id") Long id, Pageable pageable) {
        Page<OrderEntity> ordersListEntity = orderService.findAll(pageable, id);
        return ordersListEntity.map(orderMapper::mapTo);
    }

    @PostMapping("/{user_id}/orders")
    public ResponseEntity<OrderDto> createOrder(@PathVariable("user_id") Long userId, @RequestBody OrderDto orderDto) {

        UserEntity orderUser = userService
                .findOne(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        RestaurantEntity orderRestaurant = restaurantService
                .findOne(orderDto.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        OrderEntity orderEntity = orderMapper.mapFrom(orderDto);
        orderEntity.setOrderDetails(null);
        OrderEntity savedOrderEntity = orderService.save(orderEntity, orderUser, orderRestaurant);

        log.info(String.valueOf(savedOrderEntity.getOrderDetails()));
        log.info(String.valueOf(savedOrderEntity));

        List<OrderDetailEntity> savedOrderDetails = new ArrayList<>();
        for (OrderDetailDto orderDetailDto : orderDto.getOrderDetails()) {
            OrderDetailEntity newOrderDetail = orderDetailMapper.mapFrom(orderDetailDto);
            newOrderDetail.setOrderId(savedOrderEntity.getId());
            savedOrderDetails.add(newOrderDetail);
        }

        log.info(savedOrderDetails.toString());

        List<OrderDetailEntity> savedOrderDetailsList = orderDetailService.saveAll(savedOrderDetails);
        savedOrderEntity.setOrderDetails(savedOrderDetailsList);

        savedOrderEntity = orderService.save(savedOrderEntity);
        OrderDto savedOrderDto = orderMapper.mapTo(savedOrderEntity);

        return new ResponseEntity<>(savedOrderDto, HttpStatus.CREATED);
    }
}
