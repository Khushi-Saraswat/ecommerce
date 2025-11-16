package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Cart;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userrepo;

    @Autowired
    private ProductRepository productRepository;

    /*
     * @Override
     * public Cart saveCart(Integer productId, Integer userId) {
     * UserDtls userdtls = userrepo.findById(userId).get();
     * Product product = productRepository.findById(productId).get();
     * Cart cartStatus = cartRepository.findByProductIdAndUserId(productId, userId);
     * System.out.println(cartStatus + "cartStatuss");
     * Cart cart = null;
     * if (ObjectUtils.isEmpty(cartStatus)) {
     * cart = new Cart();
     * cart.setProduct(product);
     * cart.setUser(userdtls);
     * cart.setQuantity(1);
     * cart.setTotalPrice(1 * product.getDiscountPrice());
     * } else {
     * cart = cartStatus;
     * // System.out.println(cartStatus);
     * cart.setQuantity(cart.getQuantity() + 1);
     * cart.setTotalPrice(cart.getQuantity() *
     * cart.getProduct().getDiscountPrice());
     * }
     * Cart saveCart = cartRepository.save(cart);
     * return saveCart;
     * }
     */

    @Override
    public List<Cart> getCartByUsers(Integer userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);
        Double totalOrderPrice = 0.0;
        List<Cart> updateCarts = new ArrayList<>();
        for (Cart c : carts) {
            Double totalPrice = (c.getProduct().getDiscountPrice() * c.getQuantity());
            c.setTotalPrice(totalPrice);
            totalOrderPrice = totalOrderPrice + totalPrice;
            c.setTotalOrderPrice(totalOrderPrice);
            updateCarts.add(c);
        }

        return updateCarts;
    }

    @Override
    public Integer getCounterCart(Integer userId) {
        Integer countByUserId = cartRepository.countByUserId(userId);
        return countByUserId;
    }

    @Override
    public void updateQuantity(String sy, Integer cid) {
        Cart cart = cartRepository.findById(cid).get();
        Integer update;
        if (sy.equalsIgnoreCase("de")) {
            update = cart.getQuantity() - 1;
            System.out.println(update + "decrease");
            if (update <= 0) {
                cartRepository.delete(cart);
            } else {
                cart.setQuantity(update);
                cartRepository.save(cart);
            }

        } else {
            update = cart.getQuantity() + 1;
            System.out.println(update + "increase");
            cart.setQuantity(update);
            cartRepository.save(cart);
        }

    }

}
