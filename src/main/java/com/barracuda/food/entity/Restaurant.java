package com.barracuda.food.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GeneratedColumn;

import java.util.HashSet;
import java.util.Objects;
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

    Restaurant(){ }

    public Restaurant(Owner owner, String name) {
        this.owner = owner;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", owner=" + owner +
                ", name='" + name + '\'' +
                '}';
    }
}
