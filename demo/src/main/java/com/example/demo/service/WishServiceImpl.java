package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.example.demo.model.Product;
import com.example.demo.model.Wish;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WishRepository;

@Service
public class WishServiceImpl implements WishService {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private UserRepository userrepo;

    @Autowired
    private ProductRepository productRepository;

    /*
     * @Override
     * public Wish saveWish(Integer productId, Integer userId) {
     * 
     * Product product = productRepository.findById(productId).get();
     * UserDtls userdtls = userrepo.findById(userId).get();
     * System.out.println("hi" + product + "" + userdtls);
     * Wish savewish = null;
     * Wish wishStatus = wishRepository.findByProductIdAndUserId(productId, userId);
     * System.out.println(wishStatus + "wishStatus");
     * Wish wish = new Wish();
     * if (ObjectUtils.isEmpty(wishStatus)) {
     * System.out.println("in wishlist");
     * wish.setProduct(product);
     * wish.setUser(userdtls);
     * savewish = wishRepository.save(wish);
     * 
     * }
     * 
     * return savewish;
     * 
     * }
     */
    @Override
    public List<Wish> getAllWishByIdUserId(Integer UserId) {
        List<Wish> wishlist = wishRepository.findByUserId(UserId);
        return wishlist;
    }

    @Override
    public Boolean deleteWishProduct(Integer Id) {

        Product product = productRepository.findById(Id).orElse(null);
        Integer id = product.getId();
        Wish wish = wishRepository.findByProductId(id);
        if (!ObjectUtils.isEmpty(wish)) {
            wishRepository.delete(wish);
            return true;
        }
        return false;
    }

    @Override
    public List<Product> getProductsByUserId(Integer UserId) {
        List<Product> products = wishRepository.findProductByUserId(UserId);

        // List < Product > sortedList3=products.stream().sorted();
        return products;
    }

    // @Override
    // public Product GetProductByProductId(Integer ProductId){
    // Product product = productRepository.findById(ProductId).orElse(null);
    // return product;
    // }
}
