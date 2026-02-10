package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.constants.Category;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//stores all details about a product 
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "artisan_id")
    private Artisan artisan;

    @NotBlank(message = "Product name cannot be blank")
    @Size(min = 3, max = 200, message = "Product name must be between 3 and 200 characters")
    private String name;

    private String slug;

    @NotBlank(message = "Product description cannot be blank")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    private int artId;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "MRP cannot be null")
    @DecimalMin(value = "0.01", message = "MRP must be greater than 0")
    private Double mrp;

    @NotNull(message = "Active status cannot be null")
    private Boolean isActive = true;

    @NotNull(message = "Stock cannot be null")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    private Integer quantity;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Relations- 1 product as many images
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Review> reviews;

    @OneToMany(mappedBy = "product")
    private List<Cart> cartItems;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
