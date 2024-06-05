package com.rajan.foodDeliveryApp.services;

import com.rajan.foodDeliveryApp.domain.entities.MenuEntity;

import java.util.List;
import java.util.Optional;

public interface MenuService {

    MenuEntity save(MenuEntity menuEntity);

    Optional<MenuEntity> findOne(Long id);

    List<MenuEntity> findAll();

    void delete(Long id);

    boolean isExists(Long id);

}
