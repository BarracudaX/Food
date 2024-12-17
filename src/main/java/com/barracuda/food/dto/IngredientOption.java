package com.barracuda.food.dto;

import jakarta.persistence.Embeddable;

@Embeddable
public record IngredientOption(String name, int cost, int quantity, int maxQuantity) {

}
