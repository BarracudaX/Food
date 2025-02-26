package com.barracuda.food.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "branches")
@Entity
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "branches_sequence")
    private Long id;

    @ManyToOne
    private Restaurant restaurant;

    @AttributeOverrides(
            {
                    @AttributeOverride(name = "city", column = @Column(name = "city")),
                    @AttributeOverride(name = "street", column = @Column(name = "street"))
            }
    )
    private Address address;

}
