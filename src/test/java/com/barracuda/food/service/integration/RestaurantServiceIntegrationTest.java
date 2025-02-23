package com.barracuda.food.service.integration;

import com.barracuda.food.dto.RestaurantCreateForm;
import com.barracuda.food.entity.Owner;
import com.barracuda.food.repository.RestaurantRepository;
import com.barracuda.food.repository.UserRepository;
import com.barracuda.food.service.RestaurantService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

public class RestaurantServiceIntegrationTest extends AbstractServiceIntegrationTest{

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    private final Pageable pageable = Pageable.ofSize(2);

    private Owner owner;
    private RestaurantCreateForm form;

    @BeforeEach
    void setUp() {
        owner = userRepository.save(new Owner("Owner", "owner@email.com", "Password"));
        form = new RestaurantCreateForm("Restaurant",owner.getId());
    }

    @AfterEach
    void tearDown() {
        cleanDB();
    }

    @Test
    void shouldCreateNewRestaurant() {
        var assertions = new SoftAssertions();
        assertThat(restaurantRepository.findByOwnerId(owner.getId())).isEmpty();

        restaurantService.create(form);
        var restaurants = restaurantRepository.findByOwnerId(owner.getId());

        assertions.assertThat(restaurants).hasSize(1);
        assertions.assertThat(restaurants.getFirst().getName()).isEqualTo(form.name());
        assertions.assertThat(restaurants.getFirst().getOwner().getId()).isEqualTo(form.ownerID());
        assertions.assertAll();
    }

    @Test
    void shouldReturnEmptyPageOwnerRestaurants() {
        assertThat(restaurantService.restaurants(pageable,owner.getId())).isEmpty();
    }

    @Transactional
    @Test
    void shouldReturnOwnerRestaurants() {
        var assertions = new SoftAssertions();
        var res1 = restaurantService.create(form.withName("Res1"));
        var res2 = restaurantService.create(form.withName("Res2"));
        var res3 = restaurantService.create(form.withName("Res3"));

        var restaurants = restaurantService.restaurants(pageable, owner.getId());

        assertions.assertThat(restaurants.hasNext()).isTrue();
        assertions.assertThat(restaurants.hasPrevious()).isFalse();
        assertions.assertThat(restaurants.getTotalElements()).isEqualTo(3);
        assertions.assertThat(restaurants.getTotalPages()).isEqualTo(2);
        assertions.assertThat(restaurants).containsExactlyInAnyOrder(res1,res2);

        assertions.assertAll();
    }

    @Test
    void shouldThrowDataIntegrityViolationExceptionWhenCreatingRestaurantWithSameNameForSameOwner() {
        restaurantService.create(form);

        assertThatThrownBy(() -> restaurantService.create(form)).isInstanceOf(DataIntegrityViolationException.class).hasMessageContaining("UNIQUE_NAME_PER_OWNER");
    }

    @Test
    void shouldNotThrowDataIntegrityViolationExceptionWhenCreatingRestaurantWithSameNameForDifferentOwners() {
        var anotherOwner = userRepository.save(new Owner("AnotherOwner","another@email.com","password"));
        restaurantService.create(form);

        assertThatNoException().isThrownBy(() -> restaurantService.create(form.withOwnerID(anotherOwner.getId())));
    }


}
