package com.rajan.foodDeliveryApp.services;

import com.rajan.foodDeliveryApp.domain.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContentBasedRecommenderService {
    private static final double THRESHOLD = 0.70;

    private final FoodService foodService;
    private final MenuService menuService;

    @Autowired
    public ContentBasedRecommenderService(FoodService foodService, MenuService menuService) {
        this.foodService = foodService;
        this.menuService = menuService;
    }

    // Dummy data for cuisines
    private List<String> allCuisines = Arrays.asList("Nepali", "Italian", "Chinese", "American", "Japanese", "Korean", "Indian");

    public double[] createRestaurantVector(RestaurantEntity restaurant) {
        double[] vector = new double[2 + allCuisines.size()]; // Updated size for spiceLevel inclusion

        // Include cuisine encoding
        for (int i = 0; i < allCuisines.size(); i++) {
            vector[i] = restaurant.getCuisine().equals(allCuisines.get(i)) ? 1.0 : 0.0;
        }

        // Fetch foods for the restaurant and calculate the average spice level
        List<FoodEntity> foods = foodService.findFoodsByRestaurantId(restaurant.getRestaurantId());
        double averageSpiceLevel = foods.stream()
                .mapToInt(FoodEntity::getSpiceLevel)
                .average()
                .orElse(0); // Default to 0 if no spice level is found

        // Use index [allCuisines.size()] for spice level
        vector[allCuisines.size()] = averageSpiceLevel;

        return vector;
    }

    public double calculateCosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public double[] createOrderVector(List<OrderEntity> userOrders) {
        double[] combinedVector = new double[2 + allCuisines.size()]; // Updated size for spiceLevel inclusion

        for (OrderEntity order : userOrders) {
            double[] orderVector = createOrderDetailsVector(order.getOrderDetails());
            for (int i = 0; i < combinedVector.length; i++) {
                combinedVector[i] += orderVector[i]; // Summing up all order vectors
            }
        }

        double orderCount = userOrders.size();
        for (int i = 0; i < combinedVector.length; i++) {
            combinedVector[i] /= orderCount;
        }

        return combinedVector;
    }

    private double[] createOrderDetailsVector(List<OrderDetailEntity> orderDetails) {
        double[] vector = new double[2 + allCuisines.size()]; // Updated size for spiceLevel inclusion

        for (OrderDetailEntity orderDetail : orderDetails) {
            FoodEntity food = foodService.findOne(orderDetail.getFoodId()).orElseThrow(() -> new RuntimeException("Food not found"));
            MenuEntity menu = menuService.findOne(food.getMenuId()).orElseThrow(() -> new RuntimeException("Menu not found"));
            String cuisine = menu.getRestaurant().getCuisine();
            for (int i = 0; i < allCuisines.size(); i++) {
                vector[i] += cuisine.equals(allCuisines.get(i)) ? 1.0 : 0.0;
            }

            // Include spice level
            vector[allCuisines.size()] += food.getSpiceLevel();
        }

        // Normalize the vector for the number of items in the order
        int itemCount = orderDetails.size();
        for (int i = 0; i < vector.length; i++) {
            vector[i] /= itemCount;
        }

        return vector;
    }

    public List<RestaurantEntity> recommendRestaurant(List<RestaurantEntity> allRestaurants, List<OrderEntity> userOrders) {
        double[] userOrderVector = createOrderVector(userOrders);

        return allRestaurants.stream()
                .filter(restaurant -> calculateCosineSimilarity(userOrderVector, createRestaurantVector(restaurant)) >= THRESHOLD)
                .sorted(Comparator.comparingDouble(
                        restaurant -> calculateCosineSimilarity(userOrderVector,
                                createRestaurantVector((RestaurantEntity) restaurant))).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
}
