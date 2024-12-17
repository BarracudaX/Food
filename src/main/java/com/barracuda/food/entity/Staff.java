package com.barracuda.food.entity;

import com.barracuda.food.entity.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "staff")
public class Staff extends User{

    @JoinColumn(name = "branch_id")
    @ManyToOne
    private Branch branch;

    Staff(){
        super();
    }

    public Staff(String name, String email, String password) {
        super(name,email,password);
    }



}
