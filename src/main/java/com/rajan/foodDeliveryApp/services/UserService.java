package com.rajan.foodDeliveryApp.services;

import com.rajan.foodDeliveryApp.domain.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    Page<UserEntity> findAll(Pageable pageable);
}
