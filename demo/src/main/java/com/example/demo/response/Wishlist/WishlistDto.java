package com.example.demo.response.Wishlist;

import com.example.demo.request.User.UserRequestDTO;
import com.example.demo.response.Product.ProductResponseDTO;

import lombok.Data;

@Data
public class WishlistDto {

    private UserRequestDTO user;
    private ProductResponseDTO product;
}
