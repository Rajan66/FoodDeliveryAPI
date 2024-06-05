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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private static final Logger log = LoggerFactory.getLogger(MenuController.class);
    private final Mapper<MenuEntity, MenuDto> menuMapper;
    private final MenuService menuService;
    private final RestaurantService restaurantService;
    private final FoodService foodService;

    public MenuController(RestaurantService restaurantService, Mapper<MenuEntity, MenuDto> menuMapper, MenuService menuService, FoodService foodService) {
        this.restaurantService = restaurantService;
        this.menuMapper = menuMapper;
        this.menuService = menuService;
        this.foodService = foodService;
    }

    @PostMapping(path = "")
    public ResponseEntity<MenuDto> createMenu(@RequestBody MenuDto menuDto) {
        MenuEntity menuEntity = menuMapper.mapFrom(menuDto);
        log.warn(String.valueOf(menuDto.getFoodIds()));
//        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantService.findOne(menuDto.getRestaurantId());
//        RestaurantEntity restaurantEntity = optionalRestaurantEntity.orElseThrow(() -> new RuntimeException("Restaurant not found"));
//        menuEntity.setRestaurant(restaurantEntity);

//        Set<FoodEntity> foods = new HashSet<>();
//        for (Long foodId : menuDto.getFoodIds()) {
//            Optional<FoodEntity> optionalFoodEntity = foodService.findOne(foodId);
//            FoodEntity foodEntity = optionalFoodEntity.orElseThrow(() -> new RuntimeException("Food not found"));
//            foods.add(foodEntity);
//        }
//        menuEntity.setFoods(foods);

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
