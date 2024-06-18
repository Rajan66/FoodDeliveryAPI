package com.rajan.foodDeliveryApp.controllers;

import com.rajan.foodDeliveryApp.domain.dto.RestaurantDto;
import com.rajan.foodDeliveryApp.domain.entities.RestaurantEntity;
import com.rajan.foodDeliveryApp.mappers.Mapper;
import com.rajan.foodDeliveryApp.mappers.impl.RestaurantMapperImpl;
import com.rajan.foodDeliveryApp.services.RestaurantService;
import com.rajan.foodDeliveryApp.services.impl.RestaurantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/restaurants")
public class RestaurantController {

    private final Mapper<RestaurantEntity, RestaurantDto> restaurantMapper;

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantMapperImpl restaurantMapper, RestaurantServiceImpl restaurantService) {
        this.restaurantMapper = restaurantMapper;
        this.restaurantService = restaurantService;
    }


    @GetMapping(path = "")
    public Page<RestaurantDto> listRestaurants(Pageable pageable) {
        Page<RestaurantEntity> restaurantEntity = restaurantService.findAll(pageable);
        return restaurantEntity.map(restaurantMapper::mapTo);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<RestaurantDto> getRestaurant(@PathVariable Long id) {
        Optional<RestaurantEntity> foundRestaurant = restaurantService.findOne(id);
        return foundRestaurant.map(restaurantEntity -> {
            RestaurantDto restaurantDto = restaurantMapper.mapTo(restaurantEntity);
            return new ResponseEntity<>(restaurantDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(path = "")
    public ResponseEntity<RestaurantDto> createRestaurant(@RequestBody RestaurantDto restaurantDto) {
        RestaurantEntity restaurantEntity = restaurantMapper.mapFrom(restaurantDto);
        RestaurantEntity savedRestaurantEntity = restaurantService.save(restaurantEntity);
        RestaurantDto savedRestaurantDto = restaurantMapper.mapTo(savedRestaurantEntity);
        return new ResponseEntity<>(savedRestaurantDto, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<RestaurantDto> updateRestaurant(@PathVariable("id") Long id, @RequestBody RestaurantDto restaurantDto) {
        if (!restaurantService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        RestaurantEntity restaurantEntity = restaurantMapper.mapFrom(restaurantDto);
        restaurantEntity.setRestaurantId(id);
        RestaurantEntity savedRestaurantEntity = restaurantService.save(restaurantEntity);
        RestaurantDto savedRestaurantDto = restaurantMapper.mapTo(savedRestaurantEntity);
        return new ResponseEntity<>(savedRestaurantDto, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<RestaurantDto> deleteRestaurant(@PathVariable("id") Long id) {
        if (!restaurantService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        restaurantService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
