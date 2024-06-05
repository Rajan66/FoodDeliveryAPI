package com.rajan.foodDeliveryApp.controllers;


import com.rajan.foodDeliveryApp.domain.dto.FoodDto;
import com.rajan.foodDeliveryApp.domain.dto.MenuDto;
import com.rajan.foodDeliveryApp.domain.entities.FoodEntity;
import com.rajan.foodDeliveryApp.domain.entities.MenuEntity;
import com.rajan.foodDeliveryApp.domain.entities.RestaurantEntity;
import com.rajan.foodDeliveryApp.mappers.Mapper;
import com.rajan.foodDeliveryApp.services.FoodService;
import com.rajan.foodDeliveryApp.services.MenuService;
import com.rajan.foodDeliveryApp.services.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private static final Logger log = LoggerFactory.getLogger(MenuController.class);
    private final Mapper<MenuEntity, MenuDto> menuMapper;
    private final Mapper<FoodEntity, FoodDto> foodMapper;
    private final MenuService menuService;
    private final RestaurantService restaurantService;
    private final FoodService foodService;

    @Autowired
    public MenuController(RestaurantService restaurantService, Mapper<MenuEntity, MenuDto> menuMapper, Mapper<FoodEntity, FoodDto> foodMapper, MenuService menuService, FoodService foodService) {
        this.restaurantService = restaurantService;
        this.menuMapper = menuMapper;
        this.foodMapper = foodMapper;
        this.menuService = menuService;
        this.foodService = foodService;
    }

    @PostMapping(path = "/{id}")
    public ResponseEntity<MenuDto> createMenu(@PathVariable("id") Long id, @RequestBody MenuDto menuDto) {
        MenuEntity menuEntity = menuMapper.mapFrom(menuDto);

        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantService.findOne(menuDto.getRestaurant().getRestaurantId());
        RestaurantEntity restaurantEntity = optionalRestaurantEntity.orElseThrow(() -> new RuntimeException("Restaurant not found"));

        menuEntity.setMenu_id(id);
        menuEntity.setRestaurant(restaurantEntity);

        List<FoodEntity> foods = menuDto.getFoods().stream()
                .map(foodDto -> {
                    if (!foodService.isExists(foodDto.getFoodId())) {
                        // If foodId is null, create a new FoodEntity
                        FoodEntity newFood = foodMapper.mapFrom(foodDto); // Assuming you have a mapper to map FoodDto to FoodEntity
                        newFood.setMenu_id(id);
                        return foodService.save(newFood);
                    } else {
                        // If foodId is provided, find or throw exception if not found
                        return foodService.findById(foodDto.getFoodId())
                                .orElseThrow(() -> new RuntimeException("Food not found: " + foodDto.getFoodId()));
                    }
                })
                .collect(Collectors.toList());

        // Set the list of foods in the menu entity
        menuEntity.setFoods(foods);

        MenuEntity savedMenuEntity = menuService.save(menuEntity);
        MenuDto savedMenuDto = menuMapper.mapTo(savedMenuEntity);
        return new ResponseEntity<>(savedMenuDto, HttpStatus.CREATED);
    }


    @GetMapping(path = "")
    public List<MenuDto> listMenus() {
        List<MenuEntity> menuEntities = menuService.findAll();
        return menuEntities.stream().map(menuMapper::mapTo).collect(Collectors.toList());
    }
}
