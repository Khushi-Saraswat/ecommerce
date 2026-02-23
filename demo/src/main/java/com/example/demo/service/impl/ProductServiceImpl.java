package com.example.demo.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Common.AbstractMapperService;
import com.example.demo.constants.KycStatus;
import com.example.demo.constants.Role;
import com.example.demo.constants.errorTypes.AuthErrorType;
import com.example.demo.constants.errorTypes.CategoryError;
import com.example.demo.constants.errorTypes.ProductErrorType;
import com.example.demo.constants.errorTypes.UserErrorType;
import com.example.demo.exception.Auth.AuthException;
import com.example.demo.exception.Category.CategoryException;
import com.example.demo.exception.Product.ProductException;
import com.example.demo.exception.User.UserException;
import com.example.demo.model.Artisan;
import com.example.demo.model.Category;
import com.example.demo.model.PriceHistory;
import com.example.demo.model.Product;
import com.example.demo.model.ProductImage;
import com.example.demo.model.User;
import com.example.demo.repository.ArtisanRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.PriceHistoryRepo;
import com.example.demo.repository.ProductImageRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.Product.ProductRequestDTO;
import com.example.demo.response.Others.UpdateProduct;
import com.example.demo.response.Product.DeleteProductResponseDTO;
import com.example.demo.response.Product.ProductResponseDTO;
import com.example.demo.response.Product.ProductSaveResponse;
import com.example.demo.service.methods.AuthService;
import com.example.demo.service.methods.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private final UserRepository userRepository;

    @Autowired
    private AbstractMapperService abstractMapperService;

    @Autowired
    private PriceHistoryRepo priceHistoryRepo;

    @Autowired
    private ModelMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ArtisanRepository artisanRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    public ProductServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ==========================================================
    // SAVE PRODUCT
    // ==========================================================
    @Override
    public ProductSaveResponse saveProducts(ProductRequestDTO productDto, List<MultipartFile> files)
            throws IOException {

        // Authenticate user
        User user = authService.getCurrentUser();
        System.out.println(user + "in product service");
        if (user == null) {
            throw new AuthException("jwt token is missing", AuthErrorType.TOKEN_MISSING);
        }

        // Category validation
        Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());
        if (optionalCategory.isEmpty()) {
            ProductSaveResponse responseDTO = new ProductSaveResponse();
            responseDTO.setSuccess(false);
            responseDTO.setConfigurationMessage("Category not found. Please request admin to add category");
            return responseDTO;
        }

        System.out.println(user + "artisan in product");
        // Resolve artisan of logged-in user
        Artisan artisan = artisanRepository
                .findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new UserException("artisan profile not found", UserErrorType.NOT_FOUND));

        if (!artisan.getKycStatus().equals(KycStatus.APPROVED)) {
            throw new UserException("Artisan is not approved", UserErrorType.NOT_APPROVED);
        }

        // Convert DTO -> Entity
        // Product productEntity = abstractMapperService.toEntity(productDto,
        // Product.class);
        Product productEntity = objectMapper.map(productDto, Product.class);
        System.out.println("Product id before save: " + productEntity.getId());

        productEntity.setId(null);
        productEntity.setCategoryId(productDto.getCategoryId());
        productEntity.setCreatedAt(LocalDateTime.now());

        productEntity.setArtisan(artisan);
        productEntity.setArtId(artisan.getId().intValue());

        // Slug
        String slug = productEntity.getName()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-");
        productEntity.setSlug(slug);

        // Images upload
        List<ProductImage> images = new ArrayList<>();

        if (files != null && !files.isEmpty()) {

            for (MultipartFile file : files) {
                String imageUrl = cloudinaryService.uploadImage(file);

                ProductImage productImage = new ProductImage();
                productImage.setImageUrl(imageUrl);
                productImage.setProduct(productEntity);

                images.add(productImage);

            }

        }

        productEntity.setImages(images);

        // Save
        Product savedProduct = productRepository.save(productEntity);

        if (savedProduct == null) {
            throw new ProductException("Product is not saved", ProductErrorType.PRODUCT_NOT_SAVED);
        }

        ProductSaveResponse responseDTO = new ProductSaveResponse();
        responseDTO.setSuccess(true);
        responseDTO.setArtisanId(savedProduct.getArtId());
        responseDTO.setProductId(savedProduct.getId());
        responseDTO.setConfigurationMessage("Product saved successfully");

        return responseDTO;
    }

    // ==========================================================
    // GET ALL PRODUCTS (List)
    // ==========================================================

    // ==========================================================
    // GET ALL PRODUCTS (Pagination)
    // ==========================================================

    // ==========================================================
    // UPDATE PRODUCT
    // ==========================================================
    @Transactional
    @Override
    public UpdateProduct updateProduct(ProductRequestDTO dto, Integer productId, List<MultipartFile> files)
            throws IOException, Exception {

        // Authenticate user
        User user = authService.getCurrentUser();
        if (user == null) {
            throw new AuthException("jwt token is missing", AuthErrorType.TOKEN_MISSING);
        }

        // Fetch artisan
        Artisan artisan = artisanRepository
                .findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new UserException("artisan profile not found", UserErrorType.NOT_FOUND));

        if (!artisan.getKycStatus().equals(KycStatus.APPROVED)) {
            throw new UserException("Artisan is not approved", UserErrorType.NOT_APPROVED);
        }

        // Fetch product
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("Product not found", ProductErrorType.PRODUCT_NOT_FOUND));

        // Ensure ownership
        if (existingProduct.getArtisan() == null ||
                !existingProduct.getArtisan().getId().equals(artisan.getId())) {
            throw new UserException("You are not authorized to update this product", UserErrorType.UNAUTHORIZED);
        }

        if (!Boolean.TRUE.equals(existingProduct.getIsActive())) {
            throw new ProductException("Product is inactive", ProductErrorType.PRODUCT_INACTIVE);
        }

        // Category update
        if (dto.getCategoryId() != null) {
            categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new CategoryException(
                            "Category not found. Please request admin using /api/category-requests/create",
                            CategoryError.CATEGORY_NOT_FOUND));

            existingProduct.setCategoryId(dto.getCategoryId());
        }

        // Price history
        Double oldPrice = existingProduct.getPrice();

        // Update fields
        existingProduct.setName(dto.getName());
        existingProduct.setDescription(dto.getDescription());
        existingProduct.setPrice(dto.getPrice());
        existingProduct.setMrp(dto.getMrp());
        existingProduct.setStock(dto.getStock());

        // Slug update
        String slug = dto.getName()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-");
        existingProduct.setSlug(slug);

        // Save price history
        if (oldPrice != null && dto.getPrice() != null && !oldPrice.equals(dto.getPrice())) {
            PriceHistory history = new PriceHistory();
            history.setOldPrice(oldPrice);
            history.setNewPrice(dto.getPrice());
            history.setChangedAt(LocalDateTime.now());
            history.setProduct(existingProduct);
            priceHistoryRepo.save(history);
        }

        // Update images
        if (files != null && !files.isEmpty()) {

            // 1) old images remove from DB collection
            existingProduct.getImages().clear();

            // 2) add new images
            for (MultipartFile file : files) {

                String imageUrl = cloudinaryService.uploadImage(file);

                ProductImage productImage = new ProductImage();
                productImage.setImageUrl(imageUrl);
                productImage.setProduct(existingProduct);

                existingProduct.getImages().add(productImage);
            }
        }

        Product saved = productRepository.save(existingProduct);

        return abstractMapperService.toDto(saved, UpdateProduct.class);
    }

    @Override
    public DeleteProductResponseDTO DeactivateProduct(int productId) {

        User user = authService.getCurrentUser();

        if (user == null) {
            throw new AuthException("jwt token is missing", AuthErrorType.TOKEN_MISSING);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("Product not found", ProductErrorType.PRODUCT_NOT_FOUND));

        // ✅ If Admin -> delete any product
        if (user.getRole() == Role.ADMIN) {

            product.setIsActive(false);
            productRepository.save(product);

            DeleteProductResponseDTO response = new DeleteProductResponseDTO();
            response.setSuccess(true);
            response.setProductId(productId);
            response.setMessage("Product deleted successfully by admin (soft delete)");
            return response;
        }

        // ✅ If Artisan -> delete only own product
        if (user.getRole() == Role.ARTISAN) {

            Artisan artisan = artisanRepository
                    .findByUser_UserId(user.getUserId())
                    .orElseThrow(() -> new UserException("artisan profile not found", UserErrorType.NOT_FOUND));

            if (product.getArtisan() == null ||
                    !product.getArtisan().getId().equals(artisan.getId())) {
                throw new UserException("You are not authorized to delete this product", UserErrorType.UNAUTHORIZED);
            }

            product.setIsActive(false);
            productRepository.save(product);

            DeleteProductResponseDTO response = new DeleteProductResponseDTO();
            response.setSuccess(true);
            response.setProductId(productId);
            response.setMessage("Product deleted successfully (soft delete)");
            return response;
        }

        // If some other role
        throw new UserException("You are not authorized", UserErrorType.UNAUTHORIZED);

    }

    @Override
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        Page<ProductResponseDTO> productResponse = productRepository.findByIsActiveTrue(pageable).map(
                product -> abstractMapperService.toDto(product, ProductResponseDTO.class));

        return productResponse;
    }

    @Override
    public ProductResponseDTO getActiveProductById(Integer productId) {

        Product product = productRepository.findByIdAndIsActiveTrue(productId)
                .orElseThrow(() -> new ProductException("Product not found", ProductErrorType.PRODUCT_NOT_FOUND));
        return abstractMapperService.toDto(product, ProductResponseDTO.class);
    }

    @Override
    public ProductResponseDTO getActiveProductBySlug(String slug) {
        Product product = productRepository.findBySlugAndIsActiveTrue(slug)
                .orElseThrow(() -> new ProductException("Product not found", ProductErrorType.PRODUCT_NOT_FOUND));
        return abstractMapperService.toDto(product, ProductResponseDTO.class);
    }

    @Override
    public Page<ProductResponseDTO> searchProducts(String query, Pageable pageable) {

        return productRepository.findProductsBySearchText(query, pageable)
                .map(
                        p -> abstractMapperService.toDto(p, ProductResponseDTO.class));
    }

    @Override
    public Page<ProductResponseDTO> getAllActiveProductPagination(Integer pageNo, Integer PageSize, String category) {

        Pageable pageable = PageRequest.of(pageNo, PageSize);

        return productRepository.findByIsActiveTrue(pageable).map(
                p -> abstractMapperService.toDto(p, ProductResponseDTO.class));
    }

    @Override
    public Page<ProductResponseDTO> getByArtisanId(Integer id, Pageable pageable) {

        return productRepository.findByartId(id, pageable).map(
                p -> abstractMapperService.toDto(p, ProductResponseDTO.class));
    }

    @Override
    public Page<ProductResponseDTO> getProductsByCategory(String category, Pageable pageable) {

        return productRepository.findByCategory(pageable, category).map(
                p -> abstractMapperService.toDto(p, ProductResponseDTO.class));
    }

    @Override
    public DeleteProductResponseDTO toggleProductStatusByAdmin(Integer productId, Boolean active) {
        User user = authService.getCurrentUser();
        if (user == null || user.getRole() != Role.ADMIN) {
            throw new UserException("Only admin can update status", UserErrorType.UNAUTHORIZED);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(
                        "Product not found",
                        ProductErrorType.PRODUCT_NOT_FOUND));

        product.setIsActive(active);
        productRepository.save(product);

        DeleteProductResponseDTO response = new DeleteProductResponseDTO();
        response.setSuccess(true);
        response.setProductId(productId);
        response.setMessage(active ? "Product activated by admin"
                : "Product deactivated by admin");

        return response;

    }

    @Override
    public ProductResponseDTO getProductById(Integer productId) {

        User user = authService.getCurrentUser();
        if (user == null || user.getRole() != Role.ADMIN) {
            throw new UserException("Only admin can access this", UserErrorType.UNAUTHORIZED);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(
                        "Product not found",
                        ProductErrorType.PRODUCT_NOT_FOUND));

        return abstractMapperService.toDto(product, ProductResponseDTO.class);

    }

}
