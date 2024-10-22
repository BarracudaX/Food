package com.barracuda.food.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "managers")
public class Manager extends User{

    @JoinColumn(name = "restaurant_id")
    @ManyToOne
    private Restaurant restaurant;

    Manager(){
        super();
    }

    public Manager(String name,String email, String password){
        super(name,email,password,Role.MANAGER);
    }

}
