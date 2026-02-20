package com.example.demo.service.methods;

import java.util.List;

import org.apache.coyote.BadRequestException;

import com.example.demo.request.Cart.CartItemRequestDTO;
import com.example.demo.request.Cart.UpdateCartRequest;
import com.example.demo.response.Cart.CartResponse;

public interface CartService {
    public CartResponse saveCart(CartItemRequestDTO cartItemRequestDTO);

    public List<CartResponse> getCartByUsers();

    public String updateQuantity(UpdateCartRequest request);

    public String deleteCart(Integer productId) throws BadRequestException;

    public String Clearcart();
}
