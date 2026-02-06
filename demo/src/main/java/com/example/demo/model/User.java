package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.constants.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Represent a user in the application...
//UserDetails provides username, password, roles, and account status so Spring Security can authenticate and authorize the user
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Data // implement UserDetails..
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Mobile number cannot be blank")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @NotBlank(message = "Username cannot be blank")
    @Email(message = "Username must be a valid email")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotNull(message = "Role cannot be null")
    private Role role;
    // private Boolean isEnable;
    // private Boolean accountNonLocked;
    // private Integer failedAttempt;
    // private Date lockTime;
    private String resetToken;
    private String city;
    private String state;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Boolean isBlocked = false;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String name, String mobileNumber, String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.mobileNumber = mobileNumber;
        this.name = name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("roles grantedAuthority" + role);
        System.out.println(new SimpleGrantedAuthority(role.name()) + "simpleGrantedAuthority");
        return List.of(new SimpleGrantedAuthority(role.name()));

    }

    @Override
    public String getUsername() {
        return username;
    }

    // Returns the roles/permissions the user has.
    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    // String role = (roles == null || roles.trim().isEmpty()) ? "USER" :
    // roles.trim();
    // System.out.println(role+"role");
    // return List.of(new SimpleGrantedAuthority(role));
    // return List.of(new SimpleGrantedAuthority(role));
    // }

}
