package com.barracuda.food.service;

import com.barracuda.food.dto.RestaurantCreateForm;
import com.barracuda.food.entity.Restaurant;
import com.barracuda.food.repository.OwnerRepository;
import com.barracuda.food.repository.RestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final OwnerRepository ownerRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, OwnerRepository ownerRepository) {
        this.restaurantRepository = restaurantRepository;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public Restaurant create(RestaurantCreateForm form) {
        var owner = ownerRepository.getReferenceById(form.ownerID());
        var restaurant = new Restaurant(owner,form.name());

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Page<Restaurant> restaurants(Pageable pageable,long ownerID) {
        return restaurantRepository.findByOwnerId(pageable,ownerID);
    }

}
