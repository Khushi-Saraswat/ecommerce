package com.example.demo.service.methods;

import java.util.List;

import com.example.demo.response.Cart.CartResponse;

public interface CartService {
    public CartResponse saveCart(String jwt, Integer productId, Integer Stock);

    public List<CartResponse> getCartByUsers(String jwt);

    // public Integer getCounterCart(Long userId);

    // public String updateCart(User userDtls, Integer productId, String
    // updateOrDecrease);

    public Boolean deleteCart(Integer productId);

}
