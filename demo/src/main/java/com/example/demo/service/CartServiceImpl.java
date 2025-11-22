package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import com.example.demo.model.UserDtls;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.methods.CartService;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userrepo;

    @Autowired
    private ProductRepository productRepository;

    // this method is responsible for saving cart
    @Override
    public Cart saveCart(Integer productId, Long userId) {

        // get user by id
        UserDtls userDtls = userrepo.findById(userId).get();
        // get product by id
        Product product = productRepository.findById(productId).get();

        // get cart by productid and userid using springdatajpa
        Cart cartStatus = cartRepository.findByProductIdAndUserId(productId, userId);

        Cart cart = null;

        // if cart is null means save the cart
        if (ObjectUtils.isEmpty(cartStatus)) {
            cart = new Cart();
            cart.setProduct(product);
            cart.setUser(userDtls);
            cart.setQuantity(1);
            cart.setTotalPrice(1 * product.getDiscountPrice());
        } // else increase the quantity of item or calculate the total price
        else {
            cart = cartStatus;
            cart.setQuantity(cart.getQuantity() + 1);
            cart.setTotalPrice(cart.getQuantity() * cart.getProduct().getDiscountPrice());
        }
        // save the cart and return
        Cart saveCart = cartRepository.save(cart);

        return saveCart;
    }

    // this method is responsible for sending cart with totalprice added with
    // discount
    @Override
    public List<Cart> getCartByUsers(Long userId) {
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
    public Integer getCounterCart(Long userId) {
        Integer countByUserId = cartRepository.countByUserId(userId);
        return countByUserId;
    }

    // this is responsible for increasing and decreasing the quantity of product
    // added to cart
    @Override
    public void updateQuantity(String sy, Integer cid) {
        // find cart by cart id...
        Cart cart = cartRepository.findById(cid).get();
        Integer update;
        // if the quantity is decrease
        if (sy.equalsIgnoreCase("de")) {
            update = cart.getQuantity() - 1;
            System.out.println(update + "decrease");
            if (update <= 0) {
                cartRepository.delete(cart);
            } else {
                cart.setQuantity(update);
                cartRepository.save(cart);
            }

        }
        // if the quantity is increase...
        else {
            update = cart.getQuantity() + 1;
            System.out.println(update + "increase");
            cart.setQuantity(update);
            cartRepository.save(cart);
        }

    }

    // this method is responsible for deleting a product in a cart
    @Override
    public Boolean deleteCart(Integer productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            cartRepository.deleteById(product.getId());
            return true;
        }

        return false;
    }

}
