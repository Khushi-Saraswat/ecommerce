package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Common.AbstractMapperService;
import com.example.demo.constants.errorTypes.ProductErrorType;
import com.example.demo.exception.Product.ProductException;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.model.Wishlist;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.WishListRepo;
import com.example.demo.response.Wishlist.WishlistDto;
import com.example.demo.service.methods.WishlistService;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishListRepo wishListRepo;

    @Autowired
    private AbstractMapperService abstractMapperService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public WishlistDto addProductToWishlist(Integer productId) {

        WishlistDto wishlist = getWishlistByUserId();
        Wishlist wishlist2 = abstractMapperService.toEntity(wishlist, Wishlist.class);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("Product not found", ProductErrorType.PRODUCT_NOT_FOUND));
        if (wishlist2.getProducts().contains(product)) {
            wishlist2.getProducts().remove(product);
        } else
            wishlist2.getProducts().add(product);

        return abstractMapperService.toDto(wishListRepo.save(wishlist2), WishlistDto.class);
    }

    @Override
    public WishlistDto createWishlist() {
        User user = securityUtils.getCurrentUserName();
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);

        return abstractMapperService.toDto(wishListRepo.save(wishlist), WishlistDto.class);

    }

    @Override
    public WishlistDto getWishlistByUserId() {
        User user = securityUtils.getCurrentUserName();
        Wishlist wishlist = wishListRepo.findByUser_UserId(user.getUserId());
        return abstractMapperService.toDto(wishListRepo.save(wishlist), WishlistDto.class);
    }

}
