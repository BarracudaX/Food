package com.barracuda.food.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GeneratedColumn;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "restaurants")
@Getter
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "restaurant_sequence")
    private Long id;

    @ManyToOne
    private Owner owner;

    private String name;

}
