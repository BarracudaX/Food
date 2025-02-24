package com.barracuda.food.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@Data
public final class RestaurantCreateForm {

    @NotEmpty(message = "{RestaurantCreateForm.name.NotEmpty.message}")
    private String name;

    private Long ownerID;

    public RestaurantCreateForm(String name, Long ownerID) {
        this.name = name;
        this.ownerID = ownerID;
    }

    public RestaurantCreateForm withName(String name) {
        return new RestaurantCreateForm(name, ownerID);
    }

    public RestaurantCreateForm withOwnerID(long ownerID) {
        return new RestaurantCreateForm(name, ownerID);
    }

}
