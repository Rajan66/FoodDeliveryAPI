package com.rajan.foodDeliveryApp.domain.entities;


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
@Table(name = "menus")
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_id_seq")
    private Long menu_id;

    private String name;

    @OneToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @ManyToMany
    @JoinTable(
            name = "menus",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<FoodEntity> foods;

}