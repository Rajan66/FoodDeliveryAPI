package com.rajan.foodDeliveryApp.services.impl;

import com.rajan.foodDeliveryApp.config.impl.RestaurantPatcher;
import com.rajan.foodDeliveryApp.domain.entities.RestaurantEntity;
import com.rajan.foodDeliveryApp.repositories.RestaurantRepository;
import com.rajan.foodDeliveryApp.services.RestaurantService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {


    private RestaurantRepository restaurantRepository;
    private RestaurantPatcher patcher;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, RestaurantPatcher patcher) {
        this.restaurantRepository = restaurantRepository;
        this.patcher = patcher;
    }

    @Override
    public RestaurantEntity save(RestaurantEntity restaurantEntity) {
        return restaurantRepository.save(restaurantEntity);
    }

    @Override
    public RestaurantEntity save(RestaurantEntity restaurantEntity, Long id) {

        restaurantEntity.setRestaurantId(id);
        RestaurantEntity existingRestaurant = findOne(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + id));

        try {
            patcher.patch(existingRestaurant, restaurantEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return restaurantRepository.save(existingRestaurant);
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

    @Override
    public String encodeImage(byte[] imageData) {
        return Base64.getEncoder().encodeToString(imageData);
    }

}
