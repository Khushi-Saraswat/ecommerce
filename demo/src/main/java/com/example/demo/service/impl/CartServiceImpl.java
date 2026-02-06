package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Common.AbstractMapperService;
import com.example.demo.constants.errorTypes.CartErrorType;
import com.example.demo.exception.Cart.CartException;
import com.example.demo.model.Artisan;
import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.response.CartResponse;
import com.example.demo.response.UserResponseDTO;
import com.example.demo.service.methods.CartService;
import com.example.demo.service.methods.UserService;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AbstractMapperService abstractMapperService;

    public CartResponse saveCart(String jwt, Integer productId, Integer quantity) {

        try {
            // Validate input
            if (productId == null || productId <= 0) {
                throw new CartException("Invalid product ID provided", CartErrorType.INVALID_PRODUCT_ID);
            }

            if (quantity == null || quantity <= 0) {
                throw new CartException("Invalid quantity. Quantity must be greater than 0",
                        CartErrorType.INVALID_QUANTITY);
            }

            // Get user
            UserResponseDTO userDtlsDto = userService.UserByToken(jwt);

            if (userDtlsDto == null) {
                throw new CartException("User not found or invalid token", CartErrorType.INVALID_CART_OPERATION);
            }

            // Get product by id
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new CartException("Product not found with ID: " + productId,
                            CartErrorType.PRODUCT_NOT_FOUND));

            // Check stock availability
            if (product.getStock() < quantity) {
                throw new CartException(
                        "Insufficient stock. Available: " + product.getStock() + ", Requested: " + quantity,
                        CartErrorType.PRODUCT_OUT_OF_STOCK);
            }

            // Check if product is active
            if (!product.getIsActive()) {
                throw new CartException("Product is not available for purchase", CartErrorType.PRODUCT_OUT_OF_STOCK);
            }

            Artisan artisan = productRepository.findArtisanByProductId(product.getId());

            if (artisan == null) {
                throw new CartException("Artisan information not found for product",
                        CartErrorType.INVALID_CART_OPERATION);
            }

            // Check if cart item already exists
            Cart cartStatus = cartRepository.findByProductId(productId);

            if (cartStatus != null) {
                int cartQuantity = cartStatus.getQuantity() + quantity;

                // Validate total quantity doesn't exceed stock
                if (cartQuantity > product.getStock()) {
                    throw new CartException("Total quantity exceeds available stock", CartErrorType.QUANTITY_EXCEEDED);
                }

                cartStatus.setQuantity(cartQuantity);
                cartRepository.save(cartStatus);
            } else {
                Cart cart = new Cart();
                cart.setProduct(product);
                cart.setUser(abstractMapperService.toEntity(userDtlsDto, User.class));
                cart.setQuantity(quantity);
                cart.setArtisan(artisan);

                Cart c = cartRepository.save(cart);

                CartResponse cartResponse = new CartResponse();
                cartResponse.setId(c.getId());
                cartResponse.setProductName(product.getName());
                cartResponse.setQuantity(c.getQuantity());
                cartResponse.setItemTotal(product.getPrice() * cart.getQuantity());

                return cartResponse;
            }

            return buildCartResponse(cartStatus);
        } catch (CartException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new CartException("Error adding product to cart: " + ex.getMessage(),
                    CartErrorType.INVALID_CART_OPERATION);
        }
    }

    // Helper method to build CartResponse
    private CartResponse buildCartResponse(Cart cart) {
        CartResponse cartResponse = new CartResponse();
        cartResponse.setId(cart.getId());
        cartResponse.setProductName(cart.getProduct().getName());
        cartResponse.setQuantity(cart.getQuantity());
        cartResponse.setItemTotal(cart.getProduct().getPrice() * cart.getQuantity());
        return cartResponse;
    }

    // this method is responsible for sending cart with totalprice added with
    // discount

    @Override
    public List<CartResponse> getCartByUsers(String jwt) {

        try {
            // Get user
            UserResponseDTO userDtlsDto = userService.UserByToken(jwt);

            if (userDtlsDto == null) {
                throw new CartException("User not found or invalid token", CartErrorType.INVALID_CART_OPERATION);
            }

            List<Cart> carts = cartRepository.findCartsByUserId(
                    abstractMapperService.toEntity(userDtlsDto, User.class).getUserId());

            if (carts == null || carts.isEmpty()) {
                throw new CartException("Cart is empty for user", CartErrorType.CART_EMPTY);
            }

            Double totalOrderPrice = 0.0;
            List<CartResponse> cartResponses = new ArrayList<>();

            for (Cart c : carts) {
                Double totalPrice = (c.getProduct().getPrice() * c.getQuantity());
                c.setTotalPrice(totalPrice);
                totalOrderPrice = totalOrderPrice + totalPrice;
                c.setTotalOrderPrice(totalOrderPrice);

                CartResponse response = buildCartResponse(c);
                response.setTotalPrice(totalPrice);
                response.setTotalOrderPrice(totalOrderPrice);
                cartResponses.add(response);
            }

            return cartResponses;
        } catch (CartException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new CartException("Error fetching cart: " + ex.getMessage(), CartErrorType.INVALID_CART_OPERATION);
        }
    }

    // this is responsible for increasing and decreasing the quantity of product
    // added to cart

    /*
     * @Override
     * public String updateCart(User userDtls, Integer productId, String sy) {
     * 
     * // find particular cart(product) from list of carts by productid and userid
     * Cart cart = cartRepository.findByProductIdAndUserId(productId,
     * userDtls.getUserId());
     * 
     * if (cart == null) {
     * // throw exception
     * // return new IllegalArgumentException("Item not in cart");
     * }
     * 
     * Integer update;
     * // if the quantity is decrease
     * if (sy.equalsIgnoreCase("-")) {
     * // update = cart.getQuantity() - 1;
     * 
     * // if (update <= 0) {
     * // cartRepository.delete(cart);
     * // } else {
     * // cart.setQuantity(update);
     * // cartRepository.save(cart);
     * 
     * // }
     * return "Quantity decreased successfully";
     * }
     * // if the quantity is increase...
     * else {
     * // update = cart.getQuantity() + 1;
     * // cart.setQuantity(update);
     * cartRepository.save(cart);
     * return "Quantity increased successfully";
     * }
     * 
     * }
     */

    // this method is responsible for deleting a product in a cart

    @Override
    public Boolean deleteCart(Integer productId) {

        try {
            if (productId == null || productId <= 0) {
                throw new CartException("Invalid product ID provided", CartErrorType.INVALID_PRODUCT_ID);
            }

            Cart cartStatus = cartRepository.findByProductId(productId);

            if (cartStatus == null) {
                throw new CartException("Cart item not found for product ID: " + productId,
                        CartErrorType.CART_ITEM_NOT_FOUND);
            }

            cartRepository.deleteById(cartStatus.getId());
            return true;
        } catch (CartException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new CartException("Error deleting cart item: " + ex.getMessage(), CartErrorType.CART_DELETE_FAILED);
        }
    }

}
