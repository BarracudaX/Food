package com.barracuda.food.entity;

import com.barracuda.food.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    private Long id;

    @Setter
    private String name;

    private String email;

    private String password;

    User(){

    }

    public User(String name,String email,String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

}
