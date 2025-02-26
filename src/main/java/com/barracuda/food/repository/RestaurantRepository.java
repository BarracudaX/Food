package com.barracuda.food.repository;

import com.barracuda.food.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {

    List<Restaurant> findByOwnerId(Long ownerID);

    Page<Restaurant> findByOwnerId(Pageable pageable, Long ownerID);

}
