package com.barracuda.food.dto;

public record RestaurantCreateForm(String name, long ownerID) {

    public RestaurantCreateForm withName(String name){
        return new RestaurantCreateForm(name,ownerID);
    }

    public RestaurantCreateForm withOwnerID(long ownerID){
        return new RestaurantCreateForm(name,ownerID);
    }

}
