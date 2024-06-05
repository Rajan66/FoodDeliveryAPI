package com.rajan.foodDeliveryApp.controllers;

import com.rajan.foodDeliveryApp.domain.dto.FoodDto;
import com.rajan.foodDeliveryApp.domain.entities.FoodEntity;
import com.rajan.foodDeliveryApp.mappers.Mapper;
import com.rajan.foodDeliveryApp.services.FoodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/foods")
public class FoodController {


    private final Mapper<FoodEntity, FoodDto> foodMapper;

    private final FoodService foodService;

    public FoodController(Mapper<FoodEntity, FoodDto> foodMapper, FoodService foodService) {
        this.foodMapper = foodMapper;
        this.foodService = foodService;
    }


    @PostMapping(path = "")
    public ResponseEntity<FoodDto> createFood(@RequestBody FoodDto foodDto) {
        FoodEntity foodEntity = foodMapper.mapFrom(foodDto);
        FoodEntity savedFoodEntity = foodService.save(foodEntity);
        FoodDto savedFoodDto = foodMapper.mapTo(savedFoodEntity);
        return new ResponseEntity<>(savedFoodDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "")
    public List<FoodDto> listFoods() {
        List<FoodEntity> foodEntities = foodService.findAll();
        return foodEntities.stream().map(foodMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<FoodDto> getFood(@PathVariable("id") Long id) {
        Optional<FoodEntity> foundFood = foodService.findOne(id);
        return foundFood.map(foodEntity -> {
            FoodDto foodDto = foodMapper.mapTo(foodEntity);
            return new ResponseEntity<>(foodDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PutMapping(path = "/{id}")
    public ResponseEntity<FoodDto> updateFood(@PathVariable("id") Long id,@RequestBody FoodDto foodDto) {
        if(!foodService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        FoodEntity foodEntity = foodMapper.mapFrom(foodDto);
        foodEntity.setFood_id(id);
        FoodEntity savedFoodEntity = foodService.save(foodEntity);
        FoodDto savedFoodDto = foodMapper.mapTo(savedFoodEntity);
        return new ResponseEntity<>(savedFoodDto,HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<FoodDto> deleteFood(@PathVariable("id") Long id) {
        if(!foodService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        foodService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
