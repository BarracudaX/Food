package com.barracuda.food.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "staff")
public class Staff extends User{

    @JoinColumn(name = "restaurant_id")
    @ManyToOne
    private Restaurant restaurant;

    Staff(){
        super();
    }

    public Staff(String name, String email, String password) {
        super(name,email,password,Role.STAFF);
    }

}
