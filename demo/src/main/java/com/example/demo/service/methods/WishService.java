package com.example.demo.service.methods;

import java.util.List;

import com.example.demo.model.Product;
import com.example.demo.model.Wish;

public interface WishService {

    public Wish saveWish(Integer productId, Long UserId);

    public List<Wish> getAllWishByIdUserId(Long UserId);

    public Boolean deleteWishProduct(Integer Id);

    public List<Product> getProductsByUserId(Integer UserId);

    // public Product GetProductByProductId(Integer ProductId);
}
