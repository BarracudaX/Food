package com.barracuda.food.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GeneratedColumn;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "restaurant_sequence")
    private Long id;

    @AttributeOverrides(
            {
                    @AttributeOverride(name = "city", column = @Column(name = "city")),
                    @AttributeOverride(name = "street", column = @Column(name = "street"))
            }
    )
    private Address address;


}
