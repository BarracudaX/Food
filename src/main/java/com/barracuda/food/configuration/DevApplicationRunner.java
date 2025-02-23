package com.barracuda.food.configuration;

import com.barracuda.food.entity.Admin;
import com.barracuda.food.entity.Owner;
import com.barracuda.food.entity.Restaurant;
import com.barracuda.food.entity.User;
import com.barracuda.food.repository.RestaurantRepository;
import com.barracuda.food.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Profile("dev")
@Component
public class DevApplicationRunner implements ApplicationRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public DevApplicationRunner(PasswordEncoder passwordEncoder, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(userRepository.count() != 0) return;

        userRepository.save(new User("Test","test@email.com",passwordEncoder.encode("TestPass123!")));
        userRepository.save(new Admin("Admin","admin@email.com",passwordEncoder.encode("AdminPass123!")));
        var owner = userRepository.save(new Owner("Owner","owner@email.com",passwordEncoder.encode("TestPass123!")));

        IntStream.range(1,5).forEach( i -> {
            restaurantRepository.save(new Restaurant(owner,"Restaurant "+i));
        });
    }
}
