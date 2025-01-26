package com.barracuda.food;

import com.barracuda.food.entity.*;
import com.barracuda.food.entity.enums.Role;

public class Utility {

    public static String userRole(User user){
        return switch (user){
            case Admin _ -> "Admin";
            case Manager _ -> "Manager";
            case Staff _ -> "Staff";
            case Owner _ -> "Owner";
            default -> "User";
        };
    }

}
