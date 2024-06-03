package com.rajan.foodDeliveryApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rajan.foodDeliveryApp.TestDataUtil;
import com.rajan.foodDeliveryApp.domain.dto.RestaurantDto;
import com.rajan.foodDeliveryApp.domain.entities.RestaurantEntity;
import com.rajan.foodDeliveryApp.services.RestaurantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class RestaurantControllerIntegrationTests {

    private final RestaurantService restaurantService;

    private final ObjectMapper objectMapper;

    private final MockMvc mockMvc;

    @Autowired
    public RestaurantControllerIntegrationTests(RestaurantService restaurantService, ObjectMapper objectMapper, MockMvc mockMvc) {
        this.restaurantService = restaurantService;
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
    }

    @Test
    public void testThatCreateRestaurantReturnsHttp201Created() throws Exception {
        RestaurantDto restaurantDto = TestDataUtil.createTestRestaurantDtoA();
        String restaurantJson = objectMapper.writeValueAsString(restaurantDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(restaurantJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateRestaurantReturnsRestaurant() throws Exception {
        RestaurantDto restaurantDto = TestDataUtil.createTestRestaurantDtoA();
        String restaurantJson = objectMapper.writeValueAsString(restaurantDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/restaurants")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(restaurantJson)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.restaurant_id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Ram Momo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cuisine").value("Nepali"));
    }

    @Test
    @Transactional
    public void testThatListRestaurantReturnsHttp200OK() throws Exception {
        RestaurantEntity restaurantEntityA = TestDataUtil.createTestRestaurantA();
        restaurantService.save(restaurantEntityA);

        RestaurantEntity restaurantEntityB = TestDataUtil.createTestRestaurantB();
        restaurantService.save(restaurantEntityB);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @Transactional
    public void testThatListRestaurantReturnsRestaurants() throws Exception {
        RestaurantEntity restaurantEntityA = TestDataUtil.createTestRestaurantA();
        restaurantService.save(restaurantEntityA);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/restaurants")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.[2].restaurant_id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].name").value("Ram Momo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].cuisine").value("Nepali"));
    }

    @Test
    @Transactional
    public void testThatGetRestaurantReturnsHttp200OK() throws Exception {
        RestaurantEntity restaurantEntityA = TestDataUtil.createTestRestaurantA();
        restaurantService.save(restaurantEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/restaurants/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @Transactional
    public void testThatGetRestaurantReturnsRestaurant() throws Exception {
        RestaurantEntity restaurantEntityA = TestDataUtil.createTestRestaurantA();
        restaurantEntityA.setRestaurant_id(100L);
        restaurantService.save(restaurantEntityA);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/restaurants/1")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.restaurant_id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Alchemy Pizzeria"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cuisine").value("Italian"));
    }

    @Test
    @Transactional
    public void testThatUpdateRestaurantReturnsHttp200OK() throws Exception {
        RestaurantEntity restaurantEntityA = TestDataUtil.createTestRestaurantA();
        restaurantService.save(restaurantEntityA);
        restaurantEntityA.setName("Sam Momo");
        restaurantService.save(restaurantEntityA);


        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/restaurants/100")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @Transactional
    public void testThatUpdateRestaurantReturnsUpdatedRestaurant() throws Exception {
        RestaurantEntity restaurantEntityA = TestDataUtil.createTestRestaurantA();
        restaurantEntityA.setRestaurant_id(100L);
        restaurantService.save(restaurantEntityA);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/restaurants/1")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.restaurant_id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Alchemy Pizzeria"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cuisine").value("Italian"));
    }

}
