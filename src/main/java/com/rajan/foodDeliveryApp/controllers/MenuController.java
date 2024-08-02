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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PostMapping(path = "/restaurants/{restaurant_id}/menus/{id}")
    public ResponseEntity<MenuDto> createMenu(
            @PathVariable("id") Long id,
            @PathVariable("restaurant_id") Long restaurant_id,
            @RequestBody MenuDto menuDto) {
        MenuEntity menuEntity = menuMapper.mapFrom(menuDto);

        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantService.findOne(restaurant_id);
        RestaurantEntity restaurantEntity = optionalRestaurantEntity.orElseThrow(() -> new RuntimeException("Restaurant not found"));
        /* TODO create a menu first then add food on another page,
            create menu then addFood takes in one food at a time. (requires a lot of requests)
            eg: list of 20 foods, 20 requests for one restaurant, 10 restaurant * 20 requests = 200 requests = server overhead
            or take a list and add each item to the db. (bulk entry)
        * */
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

    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PatchMapping(path = "/restaurants/{restaurant_id}/menus/{id}")
    public ResponseEntity<MenuDto> addFoodToMenu(
            @PathVariable("id") Long id,
            @PathVariable("restaurantId") Long restaurantId,
            @RequestBody MenuDto menuDto) {
        MenuEntity menuEntity = menuMapper.mapFrom(menuDto);

        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantService.findOne(restaurantId);
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/menus")
    public Page<MenuDto> listMenus(Pageable pageable) {
        Page<MenuEntity> menuEntities = menuService.findAll(pageable);
        return menuEntities.map(menuMapper::mapTo);
    }

    @GetMapping(path = "/menus/{id}")
    public ResponseEntity<MenuDto> getMenu(@PathVariable("id") Long id) {
        Optional<MenuEntity> optionalMenuEntity = menuService.findOne(id);
        MenuEntity menuEntity = optionalMenuEntity.orElseThrow(() -> new IllegalArgumentException("Could not find"));
        MenuDto menuDto = menuMapper.mapTo(menuEntity);
        return new ResponseEntity<>(menuDto, HttpStatus.OK);
    }

    @GetMapping(path = "/restaurants/{restaurant_id}/menus")
    public List<MenuDto> listRestaurantMenus(@PathVariable("restaurant_id") Long restaurant_id) {
        List<MenuEntity> restaurantMenuList = menuService.getMenusByRestaurantId(restaurant_id);
        return restaurantMenuList.stream().map(menuMapper::mapTo).collect(Collectors.toList());
    }
}
