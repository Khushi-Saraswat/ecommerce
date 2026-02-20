package com.example.demo.service.methods;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.request.Product.ProductRequestDTO;
import com.example.demo.response.Others.UpdateProduct;
import com.example.demo.response.Product.DeleteProductResponseDTO;
import com.example.demo.response.Product.ProductResponseDTO;
import com.example.demo.response.Product.ProductSaveResponse;

public interface ProductService {

     public ProductSaveResponse saveProducts(ProductRequestDTO product,
               List<MultipartFile> file) throws IOException;

     // public List<ProductResponseDTO> getAllProducts();

     public Page<ProductResponseDTO> getAllProducts(Pageable pageable);

     public DeleteProductResponseDTO DeactivateProduct(int ProductId);

     // public ProductRequestDTO getProductById(Integer id);

     public UpdateProduct updateProduct(ProductRequestDTO product, Integer ProductId, List<MultipartFile> file)
               throws IOException, Exception;

     ProductResponseDTO getActiveProductById(Integer productId);

     ProductResponseDTO getActiveProductBySlug(String slug);

     Page<ProductResponseDTO> searchProducts(String query, Pageable pageable);

     // List<ProductResponseDTO> getAllActiveProducts(String category);

     // public List<ProductResponseDTO> searchProduct(String ch);

     // public Page<Product> searchProductPagination(Integer pageNo, Integer
     // pageSize, String ch);

     Page<ProductResponseDTO> getAllActiveProductPagination(Integer pageNo, Integer PageSize, String category);

     // public String IncreaseStock(Integer productId, Integer stockval);

     // public Boolean ActivateProduct(Integer productId);

     // public StockResponse StockDetails(Integer productId);

     Page<ProductResponseDTO> getByArtisanId(Integer id, Pageable pageable);

     Page<ProductResponseDTO> getProductsByCategory(String categoryId, Pageable pageable);

     // Local discovery with fallback: city -> state -> all active
     // public List<ProductRequestDTO> localDiscovery(String city, String state);

}
