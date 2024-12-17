package com.barracuda.food.entity;

import com.barracuda.food.dto.IngredientOption;
import com.barracuda.food.entity.enums.IngredientType;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "ingredients")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Ingredient  {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingredient_sequence")
    private Long id;

    private String name;

    @CollectionTable(name = "ingredient_options")
    @ElementCollection
    private Set<IngredientOption> options = new HashSet<>();

    @ManyToOne
    private Branch branch;

    @Enumerated(EnumType.STRING)
    private IngredientType type;

    protected Ingredient(){}

    public Ingredient(String name, Set<IngredientOption> options) {
        this.name = name;
        this.options.addAll(options);
    }

    public Set<IngredientOption> getOptions() {
        return Collections.unmodifiableSet(options);
    }   
}
