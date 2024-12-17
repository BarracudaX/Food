package com.barracuda.food.entity;

import jakarta.persistence.*;

@Table(name = "menu_items")
@Entity
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "menu_item_sequence")
    private Long id;

    @ManyToOne
    private Menu menu;

    private String name;

    private String description;

}
