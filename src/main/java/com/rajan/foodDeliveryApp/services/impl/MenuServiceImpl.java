package com.rajan.foodDeliveryApp.services.impl;

import com.rajan.foodDeliveryApp.domain.entities.MenuEntity;
import com.rajan.foodDeliveryApp.repositories.MenuRepository;
import com.rajan.foodDeliveryApp.services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Locale.filter;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public MenuEntity save(MenuEntity menuEntity) {
        return menuRepository.save(menuEntity);
    }

    @Override
    public Optional<MenuEntity> findOne(Long id) {
        return menuRepository.findById(id);
    }

    @Override
    public List<MenuEntity> findAll() {
        return StreamSupport.stream(menuRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }


    public List<MenuEntity> getMenusByRestaurantId(Long restaurantId) {
        return menuRepository.findByRestaurantRestaurantId(restaurantId);
    }

    @Override
    public void delete(Long id) {
        menuRepository.deleteById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return menuRepository.existsById(id);
    }
}
