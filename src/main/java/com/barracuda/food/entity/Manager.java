package com.barracuda.food.entity;

import com.barracuda.food.entity.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "managers")
public class Manager extends Staff{

    Manager(){
        super();
    }

    public Manager(String name,String email, String password){
        super(name,email,password);
    }

}
