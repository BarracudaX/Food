package com.barracuda.food.entity;

import com.barracuda.food.entity.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Table(name = "admins")
@Entity
public class Admin extends User{

    Admin(){ }

    public Admin(String name,String email,String password){
        super(name,email,password);
    }

}
