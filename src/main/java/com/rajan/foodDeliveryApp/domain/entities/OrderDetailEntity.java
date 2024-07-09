package com.rajan.foodDeliveryApp.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_details")
public class OrderDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_details_id_seq")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private FoodEntity foodId;

    private BigDecimal quantity;

    private BigDecimal price;

    @Column(name = "total_price")
    private Integer totalPrice;
}
