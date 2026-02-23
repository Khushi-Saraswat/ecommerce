package com.example.demo.service.methods;

import com.example.demo.response.Wishlist.WishlistDto;

public interface WishlistService {

    WishlistDto createWishlist();

    WishlistDto getWishlistByUserId();

    WishlistDto addProductToWishlist(Integer productId);
    // throws WishlistNotFoundException;
}
