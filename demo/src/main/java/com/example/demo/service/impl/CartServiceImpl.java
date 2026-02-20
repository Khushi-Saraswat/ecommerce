package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Common.AbstractMapperService;
import com.example.demo.constants.errorTypes.CartErrorType;
import com.example.demo.constants.errorTypes.ProductErrorType;
import com.example.demo.exception.Cart.CartException;
import com.example.demo.exception.Product.ProductException;
import com.example.demo.model.Artisan;
import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.request.Cart.CartItemRequestDTO;
import com.example.demo.request.Cart.UpdateCartRequest;
import com.example.demo.response.Cart.CartResponse;
import com.example.demo.service.methods.AuthService;
import com.example.demo.service.methods.CartService;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AbstractMapperService abstractMapperService;

    public CartResponse saveCart(CartItemRequestDTO cartItemRequestDTO) {

        try {
            // Validate input
            if (cartItemRequestDTO.getProductId() == null || cartItemRequestDTO.getProductId() <= 0) {
                throw new CartException("Invalid product ID provided", CartErrorType.INVALID_PRODUCT_ID);
            }

            if (cartItemRequestDTO.getQuantity() == null || cartItemRequestDTO.getQuantity() <= 0) {
                throw new CartException("Invalid quantity. Quantity must be greater than 0",
                        CartErrorType.INVALID_QUANTITY);
            }

            // Get user
            User user = authService.getCurrentUser();

            if (user == null) {
                throw new CartException("User not found or invalid token", CartErrorType.INVALID_CART_OPERATION);
            }

            // Get product by id
            Product product = productRepository.findById(cartItemRequestDTO.getProductId())
                    .orElseThrow(
                            () -> new CartException("Product not found with ID: " + cartItemRequestDTO.getProductId(),
                                    CartErrorType.PRODUCT_NOT_FOUND));

            // Check stock availability
            if (product.getStock() < cartItemRequestDTO.getQuantity()) {
                throw new CartException(
                        "Insufficient stock. Available: " + product.getStock() + ", Requested: "
                                + cartItemRequestDTO.getQuantity(),
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
            Cart cartStatus = cartRepository.findByProductId(cartItemRequestDTO.getProductId());

            if (cartStatus != null) {
                int cartQuantity = cartStatus.getQuantity() + cartItemRequestDTO.getQuantity();

                // Validate total quantity doesn't exceed stock
                if (cartQuantity > product.getStock()) {
                    throw new CartException("Total quantity exceeds available stock", CartErrorType.QUANTITY_EXCEEDED);
                }

                cartStatus.setQuantity(cartQuantity);
                cartRepository.save(cartStatus);
            } else {
                Cart cart = new Cart();
                cart.setProduct(product);
                cart.setUser(abstractMapperService.toEntity(user, User.class));
                cart.setQuantity(cartItemRequestDTO.getQuantity());
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
    public List<CartResponse> getCartByUsers() {

        try {
            // Get user
            User user = authService.getCurrentUser();

            if (user == null) {
                throw new CartException("User not found or invalid token", CartErrorType.INVALID_CART_OPERATION);
            }

            List<Cart> carts = cartRepository.findCartsByUserId(
                    abstractMapperService.toEntity(user, User.class).getUserId());

            if (carts == null || carts.isEmpty()) {
                throw new CartException("Cart is empty for user", CartErrorType.CART_EMPTY);
            }

            Double totalOrderPrice = 0.0;
            List<CartResponse> cartResponses = new ArrayList<>();

            for (Cart c : carts) {
                Double totalPrice = (c.getProduct()
                        .getPrice() * c.getQuantity());
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

    // this method is responsible for deleting a product in a cart

    @Override
    public String deleteCart(Integer productId) {

        User user = authService.getCurrentUser();

        if (user == null) {
            throw new CartException("User not found or invalid token", CartErrorType.INVALID_CART_OPERATION);
        }
        try {
            if (productId == null || productId <= 0) {
                throw new CartException("Invalid product ID provided", CartErrorType.INVALID_PRODUCT_ID);
            }

            Cart cartStatus = cartRepository.findByProductId(productId);

            if (cartStatus == null) {
                throw new CartException("Cart item not found for product ID: " + productId,
                        CartErrorType.CART_ITEM_NOT_FOUND);
            }

            cartRepository.delete(cartStatus);
            return "cart is deleted";
        } catch (CartException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new CartException("Error deleting cart item: " + ex.getMessage(), CartErrorType.CART_DELETE_FAILED);
        }
    }

    @Override
    public String updateQuantity(UpdateCartRequest request) {

        User user = authService.getCurrentUser();

        if (user == null) {
            throw new CartException("User not found or invalid token", CartErrorType.INVALID_CART_OPERATION);
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new ProductException("Product not found", ProductErrorType.QUANTITY_NOT_GREATER_THAN_ZERO);
        }

        Cart cartItem = cartRepository.findByUser_UserIdAndProduct_Id(user.getUserId(), request.getProductId())
                .orElseThrow(() -> new CartException("Cart item not found", CartErrorType.CART_ITEM_NOT_FOUND));

        Product product = cartItem.getProduct();

        if (product.getStock() != null && request.getQuantity() > product.getStock()) {

            throw new ProductException("Error deleting cart item: ", ProductErrorType.OUT_OF_STOCK);
        }

        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cartItem);
        return "Cart updated successfully";

    }

    @Override
    public String Clearcart() {

        User user = authService.getCurrentUser();

        if (user == null) {
            throw new CartException("User not found or invalid token", CartErrorType.INVALID_CART_OPERATION);
        }

        cartRepository.deleteByUser_UserId(user.getUserId());

        return "Cart cleared successfully";
    }

}
