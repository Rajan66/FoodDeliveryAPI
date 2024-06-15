package com.rajan.foodDeliveryApp.services;

import com.rajan.foodDeliveryApp.domain.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    List<UserEntity> findAll();
}
