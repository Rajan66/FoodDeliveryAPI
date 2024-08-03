package com.rajan.foodDeliveryApp.services;

import com.rajan.foodDeliveryApp.domain.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {

    UserEntity save(UserEntity user);

    UserEntity save(UserEntity user, Long id);

    Page<UserEntity> findAll(Pageable pageable);

    Optional<UserEntity> findOne(Long id);

    boolean isExists(Long id);

    boolean isExistsByEmail(String email);

    void delete(Long id);

}
