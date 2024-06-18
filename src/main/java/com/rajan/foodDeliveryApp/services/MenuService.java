package com.rajan.foodDeliveryApp.services;

import com.rajan.foodDeliveryApp.domain.entities.MenuEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MenuService {

    MenuEntity save(MenuEntity menuEntity);

    Optional<MenuEntity> findOne(Long id);

    Page<MenuEntity> findAll(Pageable pageable);

    List<MenuEntity> getMenusByRestaurantId(Long id);

    void delete(Long id);

    boolean isExists(Long id);

}
