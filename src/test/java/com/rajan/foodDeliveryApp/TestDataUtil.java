package com.rajan.foodDeliveryApp;

import com.rajan.foodDeliveryApp.domain.dto.RestaurantDto;
import com.rajan.foodDeliveryApp.domain.entities.RestaurantEntity;

public final class TestDataUtil {

    private TestDataUtil() {

    }

    public static RestaurantDto createTestRestaurantDtoA() {
        return RestaurantDto.builder()
                .name("Ram Momo")
                .cuisine("Nepali")
                .build();
    }

    public static RestaurantDto createTestRestaurantDtoB() {
        return RestaurantDto.builder()
                .name("Gourmet Eatery")
                .cuisine("Burger")
                .build();
    }

    public static RestaurantEntity createTestRestaurantA() {
        return RestaurantEntity.builder()
                .name("Ram Momo")
                .cuisine("Nepali")
                .build();
    }

    public static RestaurantEntity createTestRestaurantB() {
        return RestaurantEntity.builder()
                .name("Gourmet Eatery")
                .cuisine("Burger")
                .build();
    }
}
