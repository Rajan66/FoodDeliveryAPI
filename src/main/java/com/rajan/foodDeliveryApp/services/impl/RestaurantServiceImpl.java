package com.rajan.foodDeliveryApp.services.impl;

import com.rajan.foodDeliveryApp.domain.entities.RestaurantEntity;
import com.rajan.foodDeliveryApp.repositories.RestaurantRepository;
import com.rajan.foodDeliveryApp.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import java.util.stream.StreamSupport;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public RestaurantEntity save(RestaurantEntity restaurantEntity) {
        return restaurantRepository.save(restaurantEntity);
    }

    @Override
    public Optional<RestaurantEntity> findOne(Long id) {
        return restaurantRepository.findById(id);
    }

    @Override
    public Page<RestaurantEntity> findAll(Pageable pageable) {
        return restaurantRepository.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        restaurantRepository.deleteById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return restaurantRepository.existsById(id);
    }
}
