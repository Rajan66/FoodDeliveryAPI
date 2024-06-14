package com.rajan.foodDeliveryApp.services;

import com.rajan.foodDeliveryApp.domain.entities.RestaurantEntity;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {
    RestaurantEntity save(RestaurantEntity restaurantEntity);

    Optional<RestaurantEntity> findOne(Long id);

    List<RestaurantEntity> findAll();

    void delete(Long id);

    boolean isExists(Long id);

}
