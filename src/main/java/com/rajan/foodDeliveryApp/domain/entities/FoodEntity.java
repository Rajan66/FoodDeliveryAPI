package com.rajan.foodDeliveryApp.domain.entities;


import com.rajan.foodDeliveryApp.config.FoodCategory;
import jakarta.persistence.*;
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
@Entity
@Table(name = "foods")
public class FoodEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "food_id_seq")
    private Long food_id;

    private String name;

    private String category;

    @OneToMany(mappedBy = "foods")
    private List<MenuEntity> menus;
}
