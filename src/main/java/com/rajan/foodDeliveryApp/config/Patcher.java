package com.rajan.foodDeliveryApp.config;

import com.rajan.foodDeliveryApp.domain.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * Class to help in patching the data, default save service overrides the data to null if not passed
 * which defeats the whole purpose of using patch, so we assign the previous value if no value is given in the request.
 **/
@Component
public class Patcher {
    public static void userPatcher(UserEntity existingUser, UserEntity incompleteUser) throws IllegalAccessException {
        Class<?> userEntityClass = UserEntity.class;
        Field[] userFields = userEntityClass.getDeclaredFields();
        for (Field field : userFields) {
            field.setAccessible(true);
            Object value = field.get(incompleteUser);
            if (value != null) {
                field.set(existingUser, value);
            }
            field.setAccessible(false);
        }

    }
}