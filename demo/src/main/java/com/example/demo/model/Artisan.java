package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.constants.KycStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "artisans")
public class Artisan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User cannot be null")
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank(message = "Brand name cannot be blank")
    @Size(min = 3, max = 100, message = "Brand name must be between 3 and 100 characters")
    private String brandName;

    @NotBlank(message = "Artisan type cannot be blank")
    @Size(min = 2, max = 50, message = "Artisan type must be between 2 and 50 characters")
    private String artisianType;

    @NotBlank(message = "Bio cannot be blank")
    @Size(min = 10, max = 500, message = "Bio must be between 10 and 500 characters")
    private String bio;

    @NotBlank(message = "City cannot be blank")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    private String city;

    @NotBlank(message = "State cannot be blank")
    @Size(min = 2, max = 50, message = "State must be between 2 and 50 characters")
    private String state;

    @NotBlank(message = "Pincode cannot be blank")
    @Pattern(regexp = "^\\d{6}$", message = "Pincode must be 6 digits")
    private String pincode;

    @NotNull(message = "KYC status cannot be null")
    private KycStatus kycStatus;

    private LocalDateTime localDateTime = LocalDateTime.now();

    @OneToMany(mappedBy = "artisan")
    private List<Product> products;

    @OneToMany(mappedBy = "artisan")
    private List<ArtisanOrder> artisanOrders;

}
