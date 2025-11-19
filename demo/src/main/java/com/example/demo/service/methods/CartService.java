package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Cart;

public interface CartService {
    public Cart saveCart(Integer productId, Long userId);

    public List<Cart> getCartByUsers(Long userId);

    public Integer getCounterCart(Long userId);

    public void updateQuantity(String sy, Integer cid);

    public Boolean deleteCart(Integer productId);
}
