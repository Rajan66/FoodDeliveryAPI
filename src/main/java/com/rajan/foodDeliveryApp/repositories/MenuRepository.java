package com.rajan.foodDeliveryApp.repositories;

import com.rajan.foodDeliveryApp.domain.dto.MenuDto;
import com.rajan.foodDeliveryApp.domain.entities.MenuEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends CrudRepository<MenuEntity, Long> {

}
