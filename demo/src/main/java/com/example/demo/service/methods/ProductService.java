package com.example.demo.service.methods;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Product;

public interface ProductService {

    public Product saveProduct(Product product);

    public List<Product> getAllProducts();

    public Page<Product> getAllProducts(Integer pageNo, Integer pageSize);

    public Boolean deleteProduct(int id);

    public Product getProductById(Integer id);

    public Product updateProduct(Product product, MultipartFile image);

    public List<Product> getAllActiveProducts(String category);

    public List<Product> searchProduct(String ch);

    public Page<Product> searchProductPagination(Integer pageNo, Integer pageSize, String ch);

    public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer PageSize, String category);
}
