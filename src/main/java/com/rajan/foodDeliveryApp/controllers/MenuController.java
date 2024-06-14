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
@RequestMapping("/api")
public class MenuController {

    private static final Logger log = LoggerFactory.getLogger(MenuController.class);
    private final Mapper<MenuEntity, MenuDto> menuMapper;
    private final Mapper<FoodEntity, FoodDto> foodMapper;
    private final MenuService menuService;
    private final RestaurantService restaurantService;
    private final FoodService foodService;

    public MenuController(RestaurantService restaurantService, Mapper<MenuEntity, MenuDto> menuMapper, Mapper<FoodEntity, FoodDto> foodMapper, MenuService menuService, FoodService foodService) {
        this.restaurantService = restaurantService;
        this.menuMapper = menuMapper;
        this.foodMapper = foodMapper;
        this.menuService = menuService;
        this.foodService = foodService;
    }

    @PostMapping(path = "/restaurants/{restaurant_id}/menus/{id}")
    public ResponseEntity<MenuDto> createMenu(
            @PathVariable("id") Long id,
            @PathVariable("restaurant_id") Long restaurant_id,
            @RequestBody MenuDto menuDto) {
        MenuEntity menuEntity = menuMapper.mapFrom(menuDto);

        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantService.findOne(restaurant_id);
        RestaurantEntity restaurantEntity = optionalRestaurantEntity.orElseThrow(() -> new RuntimeException("Restaurant not found"));

        menuEntity.setMenu_id(id);
        menuEntity.setRestaurant(restaurantEntity);

        List<FoodEntity> foods = menuDto.getFoods().stream()
                .map(foodDto -> {
                    if (!foodService.isExists(foodDto.getFoodId())) {
                        // If foodId is null, create a new FoodEntity
                        FoodEntity newFood = foodMapper.mapFrom(foodDto);
                        newFood.setMenu_id(id);
                        return foodService.save(newFood);
                    } else {
                        // If foodId is provided, find or throw exception if not found
                        return foodService.findById(foodDto.getFoodId())
                                .orElseThrow(() -> new RuntimeException("Food not found: " + foodDto.getFoodId()));
                    }
                })
                .collect(Collectors.toList());

        menuEntity.setFoods(foods);

        MenuEntity savedMenuEntity = menuService.save(menuEntity);
        MenuDto savedMenuDto = menuMapper.mapTo(savedMenuEntity);
        return new ResponseEntity<>(savedMenuDto, HttpStatus.CREATED);
    }

    @PatchMapping(path = "/restaurants/{restaurant_id}/menus/{id}")
    public ResponseEntity<MenuDto> addFoodToMenu(
            @PathVariable("id") Long id,
            @PathVariable("restaurant_id") Long restaurant_id,
            @RequestBody MenuDto menuDto) {
        MenuEntity menuEntity = menuMapper.mapFrom(menuDto);

        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantService.findOne(restaurant_id);
        RestaurantEntity restaurantEntity = optionalRestaurantEntity.orElseThrow(() -> new RuntimeException("Restaurant not found"));

        menuEntity.setMenu_id(id);
        menuEntity.setRestaurant(restaurantEntity);

        List<FoodEntity> foods = menuDto.getFoods().stream()
                .filter(foodDto -> !foodService.isExists(foodDto.getFoodId())) // Filter foods that do not exist
                .map(foodMapper::mapFrom) // Map FoodDto to FoodEntity using foodMapper
                .peek(foodEntity -> foodEntity.setMenu_id(id)) // Set menu_id for each FoodEntity
                .map(foodService::save) // Save each FoodEntity
                .toList();

        menuEntity.setFoods(foods);
        MenuEntity savedMenuEntity = menuService.save(menuEntity);
        MenuDto savedMenuDto = menuMapper.mapTo(savedMenuEntity);

        return new ResponseEntity<>(savedMenuDto, HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/menus")
    public List<MenuDto> listMenus() {
        List<MenuEntity> menuEntities = menuService.findAll();
        return menuEntities.stream().map(menuMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path = "/restaurants/{restaurant_id}/menus")
    public List<MenuDto> listRestaurantMenus(@PathVariable("restaurant_id") Long restaurant_id) {
        List<MenuEntity> restaurantMenuList = menuService.getMenusByRestaurantId(restaurant_id);
        return restaurantMenuList.stream().map(menuMapper::mapTo).collect(Collectors.toList());
    }
}
