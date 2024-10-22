package com.barracuda.food.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    private Long id;

    @NotEmpty
    private String name;

    @NotNull
    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    User(){

    }

    public User(String name,String email,String password){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = Role.USER;
    }

    protected User(String name,String email, String password,Role role){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
