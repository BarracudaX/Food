package com.barracuda.food.entity;

import com.barracuda.food.entity.enums.Role;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Table(name = "restaurant_owners")
@Entity
public class Owner extends User {

    Owner(){
        super();
    }

    public Owner(String name, String email,String password){
        super(name,email,password);
    }
}
