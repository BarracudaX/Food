package com.barracuda.food.entity;

import com.barracuda.food.entity.enums.MenuType;
import jakarta.persistence.*;


@Table(name = "menus")
@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "menu_sequence")
    private Long id;

    @ManyToOne
    private Branch branch;

    private String name;

    @Column(name = "menu_type")
    @Enumerated(EnumType.STRING)
    private MenuType menuType;

}
