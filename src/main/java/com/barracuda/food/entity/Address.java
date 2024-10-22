package com.barracuda.food.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public record Address(String city,String street) {
}
