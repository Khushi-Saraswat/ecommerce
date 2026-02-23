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

     ProductSaveResponse saveProducts(ProductRequestDTO product,
               List<MultipartFile> file) throws IOException;

     // public List<ProductResponseDTO> getAllProducts();

     Page<ProductResponseDTO> getAllProducts(Pageable pageable);

     DeleteProductResponseDTO DeactivateProduct(int ProductId);

     ProductResponseDTO getProductById(Integer productId);

     UpdateProduct updateProduct(ProductRequestDTO product, Integer ProductId, List<MultipartFile> file)
               throws IOException, Exception;

     ProductResponseDTO getActiveProductById(Integer productId);

     ProductResponseDTO getActiveProductBySlug(String slug);

     Page<ProductResponseDTO> searchProducts(String query, Pageable pageable);

     Page<ProductResponseDTO> getAllActiveProductPagination(Integer pageNo, Integer PageSize, String category);

     Page<ProductResponseDTO> getByArtisanId(Integer id, Pageable pageable);

     Page<ProductResponseDTO> getProductsByCategory(String categoryId, Pageable pageable);

     DeleteProductResponseDTO toggleProductStatusByAdmin(Integer productId, Boolean active);

}
