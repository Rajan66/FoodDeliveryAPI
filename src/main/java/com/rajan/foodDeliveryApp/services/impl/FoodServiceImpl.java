package com.rajan.foodDeliveryApp.services.impl;

import com.rajan.foodDeliveryApp.domain.entities.FoodEntity;
import com.rajan.foodDeliveryApp.repositories.FoodRepository;
import com.rajan.foodDeliveryApp.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Override
    public FoodEntity save(FoodEntity foodEntity) {
        return foodRepository.save(foodEntity);
    }

    @Override
    public Optional<FoodEntity> findOne(Long id) {
        return foodRepository.findById(id);
    }

    @Override
    public List<FoodEntity> findAll() {
        return StreamSupport.stream(foodRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        foodRepository.deleteById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return foodRepository.existsById(id);
    }
}
