package com.rajan.foodDeliveryApp.services;

import com.rajan.foodDeliveryApp.domain.entities.FoodEntity;

import java.util.List;
import java.util.Optional;

public interface FoodService {

    FoodEntity save(FoodEntity foodEntity);

    Optional<FoodEntity> findOne(Long id);

    List<FoodEntity> findAll();

    Optional<FoodEntity> findById(Long id);

    void delete(Long id);

    boolean isExists(Long id);

}
