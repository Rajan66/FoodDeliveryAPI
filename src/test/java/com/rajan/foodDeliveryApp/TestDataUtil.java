package com.rajan.foodDeliveryApp;

import com.rajan.foodDeliveryApp.domain.dto.RestaurantDto;
import com.rajan.foodDeliveryApp.domain.entities.RestaurantEntity;

public final class TestDataUtil {

    private TestDataUtil() {

    }

    public static RestaurantDto createTestRestaurantDtoA() {
        return RestaurantDto.builder()
                .restaurant_id(100L)
                .name("Ram Momo")
                .cuisine("Nepali")
                .build();
    }

    public static RestaurantDto createTestRestaurantDtoB() {
        return RestaurantDto.builder()
                .restaurant_id(100L)
                .name("Gourmet Eatery")
                .cuisine("Burger")
                .build();
    }

    public static RestaurantEntity createTestRestaurantA() {
        return RestaurantEntity.builder()
                .restaurant_id(100L)
                .name("Ram Momo")
                .cuisine("Nepali")
                .build();
    }

    public static RestaurantEntity createTestRestaurantB() {
        return RestaurantEntity.builder()
                .restaurant_id(100L)
                .name("Gourmet Eatery")
                .cuisine("Burger")
                .build();
    }
}
