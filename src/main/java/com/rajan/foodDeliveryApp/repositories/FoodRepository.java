package com.rajan.foodDeliveryApp.repositories;

import com.rajan.foodDeliveryApp.domain.entities.FoodEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends CrudRepository<FoodEntity, Long> {
}
