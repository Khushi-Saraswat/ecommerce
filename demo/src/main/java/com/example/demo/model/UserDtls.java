package com.example.demo.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//UserDetails provides username, password, roles, and account status so Spring Security can authenticate and authorize the user
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class UserDtls implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String mobileNumber;
    private String username;
    private String password;
    private String roles;
    private Boolean isEnable;
    private Boolean accountNonLocked;
    private Integer failedAttempt;
    private Date lockTime;
    private String resetToken;
    private String profileimage;

    public UserDtls(String username, String password, String roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public UserDtls(String name, String mobileNumber, String username, String password, String roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.mobileNumber = mobileNumber;
        this.name = name;
    }

    // Returns the roles/permissions the user has.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = (roles == null || roles.trim().isEmpty()) ? "USER" : roles.trim();

        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {

        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return password;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return username;
    }
}
