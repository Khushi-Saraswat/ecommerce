package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Product;
import com.example.demo.model.Wish;

public interface WishService {

    // public Wish saveWish(Integer productId, Integer UserId);

    public List<Wish> getAllWishByIdUserId(Integer UserId);

    public Boolean deleteWishProduct(Integer Id);

    public List<Product> getProductsByUserId(Integer UserId);

    // public Product GetProductByProductId(Integer ProductId);
}
