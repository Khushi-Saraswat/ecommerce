package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//can be used to group products into categories.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Category {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private int id;
      private String name;
      private String imageName;
      private Boolean isActive;
      private LocalDateTime createdAt;
      private LocalDateTime updatedAt;

}
