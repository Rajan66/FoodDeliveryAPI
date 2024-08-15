package com.rajan.foodDeliveryApp.controllers;

import com.rajan.foodDeliveryApp.domain.entities.OrderEntity;
import com.rajan.foodDeliveryApp.domain.entities.RestaurantEntity;
import com.rajan.foodDeliveryApp.repositories.RestaurantRepository;
import com.rajan.foodDeliveryApp.services.ContentBasedRecommenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private ContentBasedRecommenderService recommenderService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @PostMapping("/restaurant")
    public RestaurantEntity recommendRestaurant(@RequestBody OrderEntity userOrder) {
        List<RestaurantEntity> allRestaurants = restaurantRepository.findAll();
        return recommenderService.recommendRestaurant(allRestaurants, userOrder);
    }
}
