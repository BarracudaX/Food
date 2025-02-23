package com.barracuda.food.service;

import com.barracuda.food.dto.RestaurantCreateForm;
import com.barracuda.food.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantService {

    Restaurant create(RestaurantCreateForm form);

    Page<Restaurant> restaurants(Pageable pageable, long ownerID);

}
