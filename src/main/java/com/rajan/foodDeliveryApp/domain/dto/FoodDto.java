package com.rajan.foodDeliveryApp.domain.dto;

import com.rajan.foodDeliveryApp.config.FoodCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodDto {

    private Long foodId;

    private String name;

    private FoodCategory category;

    private List<MenuDto> menus;
}
