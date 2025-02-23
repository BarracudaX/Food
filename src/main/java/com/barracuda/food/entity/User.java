package com.barracuda.food.entity;

import com.barracuda.food.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    private Long id;

    @Setter
    private String name;

    private String email;

    private String password;

    User(){

    }

    public User(String name,String email,String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public final Collection<? extends GrantedAuthority> getAuthorities() {
        var role = switch (this){
            case Admin _ -> "ROLE_"+Role.ADMIN.name();
            case Owner _ -> "ROLE_"+Role.OWNER.name();
            case Manager _ -> "ROLE_"+Role.MANAGER.name();
            case Staff _ -> "ROLE_"+Role.STAFF.name();
            default -> "ROLE_"+Role.USER.name();
        };

        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }
}
