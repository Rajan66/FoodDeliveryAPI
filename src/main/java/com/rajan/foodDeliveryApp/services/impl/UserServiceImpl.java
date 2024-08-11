package com.rajan.foodDeliveryApp.services.impl;

import com.rajan.foodDeliveryApp.config.Patcher;
import com.rajan.foodDeliveryApp.domain.entities.UserEntity;
import com.rajan.foodDeliveryApp.repositories.UserRepository;
import com.rajan.foodDeliveryApp.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Patcher patcher;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, Patcher patcher) {
        this.userRepository = userRepository;
        this.patcher = patcher;
    }


    @Override
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public UserEntity save(UserEntity user, Long id) {
        user.setId(id);
        UserEntity existingUser = findOne(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        try {
            patcher.userPatcher(existingUser, user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userRepository.save(existingUser);
    }

    @Override
    public Page<UserEntity> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<UserEntity> findOne(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }


}
