package com.barracuda.food.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Table(name = "restaurant_owners")
@Entity
public class Owner extends User{

    @ManyToMany
    @JoinTable(name = "restaurant_ownership",joinColumns = {@JoinColumn(name = "owner_id")},inverseJoinColumns = { @JoinColumn(name = "restaurant_id") })
    private Set<Restaurant> restaurants = new HashSet<>();

    Owner(){
        super();
    }

    public Owner(String name, String email,String password){
        super(name,email,password,Role.OWNER);
    }
}
