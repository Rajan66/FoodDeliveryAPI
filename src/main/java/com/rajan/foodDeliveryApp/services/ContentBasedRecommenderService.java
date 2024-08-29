package com.rajan.foodDeliveryApp.services;

import com.rajan.foodDeliveryApp.domain.entities.OrderEntity;
import com.rajan.foodDeliveryApp.domain.entities.RestaurantEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class ContentBasedRecommenderService {
    // Dummy data
    private List<String> allCuisines = Arrays.asList("Nepali", "Italian", "Chinese", "American", "Japanese", "Korean", "Indian");

    public double[] createRestaurantVector(RestaurantEntity restaurant) {
        double[] vector = new double[2 + allCuisines.size()];

        vector[0] = restaurant.getAveragePrice() / 1000.0;

        for (int i = 0; i < allCuisines.size(); i++) {
            vector[i + 1] = restaurant.getCuisine().equals(allCuisines.get(i)) ? 1.0 : 0.0;
        }
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

    public double[] createOrderVector(OrderEntity order) {
        RestaurantEntity restaurant = order.getRestaurant();
        return createRestaurantVector(restaurant);
    }

    public RestaurantEntity recommendRestaurant(List<RestaurantEntity> allRestaurants, OrderEntity userOrder) {
        double[] userOrderVector = createOrderVector(userOrder);

        return allRestaurants.stream()
                .max(Comparator.comparingDouble(
                        restaurant -> calculateCosineSimilarity(userOrderVector,
                                createRestaurantVector(restaurant))))
                .orElse(null);
    }
}
