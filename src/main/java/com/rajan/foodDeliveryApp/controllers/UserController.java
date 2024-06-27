package com.rajan.foodDeliveryApp.controllers;

import com.rajan.foodDeliveryApp.domain.dto.UserDto;
import com.rajan.foodDeliveryApp.domain.entities.UserEntity;
import com.rajan.foodDeliveryApp.mappers.Mapper;
import com.rajan.foodDeliveryApp.mappers.impl.UserMapperImpl;
import com.rajan.foodDeliveryApp.repositories.UserRepository;
import com.rajan.foodDeliveryApp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final Mapper<UserEntity, UserDto> userMapper;

    public UserController(UserService userService, UserMapperImpl userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @GetMapping("/users")
    public Page<UserDto> listUsers(Pageable pageable) {
        Page<UserEntity> usersList = userService.findAll(pageable);
        return usersList.map(userMapper::mapTo);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
        if (userService.isExists(id)) {
            return ResponseEntity.notFound().build();
        }
        UserEntity userEntity = userMapper.mapFrom(userDto);
        UserEntity updatedUser = userService.save(userEntity);
        return ResponseEntity.ok(userMapper.mapTo(updatedUser));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable("id") Long id) {
        if (userService.isExists(id)) {
            return ResponseEntity.notFound().build();
        }
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}