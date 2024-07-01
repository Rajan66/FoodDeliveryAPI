package com.rajan.foodDeliveryApp.services;

import com.rajan.foodDeliveryApp.domain.entities.DeliveryPartnerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface DeliveryPartnerService {
    DeliveryPartnerEntity save(DeliveryPartnerEntity deliveryPartnerEntity);

    Page<DeliveryPartnerEntity> findAll(Pageable pageable);
}
