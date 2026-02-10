package com.example.demo.service.methods;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.Products;
import com.example.demo.model.Product;
import com.example.demo.response.Product.ProductSaveResponse;
import com.example.demo.response.Others.StockResponse;
import com.example.demo.response.Others.UpdateProduct;

public interface ProductService {

    public ProductSaveResponse saveProducts(Products product, List<MultipartFile> file) throws IOException;

    public List<Products> getAllProducts();

    public Page<Product> getAllProducts(Integer pageNo, Integer pageSize);

    public Boolean DeactivateProduct(int ProductId);

    public Products getProductById(Integer id);

    public UpdateProduct updateProduct(Products product, Integer ProductId,List<MultipartFile> file) throws IOException;
            

    //public List<Product> getAllActiveProducts( category);

    // public List<ProductDto> searchProduct(String ch);

    // public Page<Product> searchProductPagination(Integer pageNo, Integer
    // pageSize, String ch);

    public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer PageSize, String category);

    public String IncreaseStock(Integer productId, Integer stockval);

    public Boolean ActivateProduct(Integer productId);

    public StockResponse StockDetails(Integer productId);

    public List<Products> getByArtisanId(String jwt);

    // Local discovery with fallback: city -> state -> all active
    public List<Products> localDiscovery(String city, String state);

}
