package com.example.demo.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Common.AbstractMapperService;
import com.example.demo.Specification.JpaSpecification;
import com.example.demo.constants.KycStatus;
import com.example.demo.constants.Role;
import com.example.demo.constants.errorTypes.AuthErrorType;
import com.example.demo.constants.errorTypes.ProductErrorType;
import com.example.demo.constants.errorTypes.UserErrorType;
import com.example.demo.exception.Auth.AuthException;
import com.example.demo.exception.Product.ProductException;
import com.example.demo.exception.User.UserException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.request.Product.ProductRequestDTO;
import com.example.demo.response.Others.UpdateProduct;
import com.example.demo.response.Product.DeleteProductResponseDTO;
import com.example.demo.response.Product.ProductResponseDTO;
import com.example.demo.response.Product.ProductSaveResponse;
import com.example.demo.service.methods.AuthService;
import com.example.demo.service.methods.ProductService;
import com.example.demo.service.impl.CacheService.ProductCacheService; // Added import

import lombok.RequiredArgsConstructor; // Added for cleaner injection

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
    private ProductCacheService productCacheService;

    //  private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    public ProductServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ==========================================================
    // SAVE PRODUCT
    // ==========================================================
    @CacheEvict(
          value = {
          "activeProduct",
           "productSlug",
          "categoryProducts",
           "searchProducts"
          },
        allEntries = true
    )
    @Override
    public ProductSaveResponse saveProducts(ProductRequestDTO productDto, List<MultipartFile> files)
            throws IOException {

        System.out.println("saveProducts:" + productDto);
        // Authenticate user
        User user = authService.getCurrentUser();
        System.out.println(user + "in product service");
        if (user == null) {
            throw new AuthException("jwt token is missing", AuthErrorType.TOKEN_MISSING);
        }

        // Category validation - handle multiple categories with same name
        List<Category> categories = categoryRepository.findByName(productDto.getCategoryName());
        if (categories.isEmpty()) {
            ProductSaveResponse responseDTO = new ProductSaveResponse();
            responseDTO.setSuccess(false);
            responseDTO.setConfigurationMessage("Category not found. Please request admin to add category");
            return responseDTO;
        }
        // Get first active category or first one available
        Category category = categories.stream()
                .filter(cat -> cat.getIsActive() != null && cat.getIsActive())
                .findFirst()
                .orElse(categories.get(0));

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

        productEntity.setCreatedAt(LocalDateTime.now());

        productEntity.setArtisan(artisan);
        productEntity.setArtId(artisan.getId().intValue());

        // Slug
        String slug = productEntity.getName()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-");
        productEntity.setSlug(slug);
        productEntity.setCategory(category);

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

    @CacheEvict(
      value = {
      "activeProduct",
      "productSlug",
       "categoryProducts",
       "searchProducts"
     },
    allEntries = true
     )
    @Transactional
    @Override
    public UpdateProduct updateProduct(ProductRequestDTO dto, Integer productId, List<MultipartFile> files)
            throws IOException, Exception {

        // Authenticate user
        User user = authService.getCurrentUser();
        if (user == null) {
            throw new AuthException("jwt token is missing", AuthErrorType.TOKEN_MISSING);
        }

        System.out.println("product" + "" + dto);
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
        // if (dto.getCategoryId() != null) {
        // categoryRepository.findById(dto.getCategoryId())
        // .orElseThrow(() -> new CategoryException(
        // "Category not found. Please request admin using
        // /api/category-requests/create",
        // CategoryError.CATEGORY_NOT_FOUND));

        // existingProduct.setCategoryId(dto.getCategoryId());
        // }

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

    @CacheEvict(
     value = {
    "activeProduct",
    "productSlug",
    "categoryProducts",
     "searchProducts"
     },
    allEntries = true
    )
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


    

    @Transactional
    @Override
    public ProductResponseDTO getActiveProductById(Integer productId) {

        System.out.println("db call hua hai");

        Product product = productRepository.findByIdAndIsActiveTrue(productId)
                .orElseThrow(() -> new ProductException("Product not found", ProductErrorType.PRODUCT_NOT_FOUND));
        
        System.out.println(product.getName()+"product");
        return abstractMapperService.toDto(product, ProductResponseDTO.class);
    }

    @Cacheable(value = "productSlug", key = "#slug")
    @Override
    public ProductResponseDTO getActiveProductBySlug(String slug) {
        Product product = productRepository.findBySlugAndIsActiveTrue(slug)
                .orElseThrow(() -> new ProductException("Product not found", ProductErrorType.PRODUCT_NOT_FOUND));
        return abstractMapperService.toDto(product, ProductResponseDTO.class);
    }

    @Cacheable(
     value = "searchProducts",
    key = "#query + '-' + #price + '-' + #mrp + '-' + #pageable.pageNumber"
    )
    @Override
    public Page<ProductResponseDTO> searchProducts(String query, Pageable pageable, Double price,
            Double mrp) {

        Page<ProductResponseDTO> page = productRepository.findAll(JpaSpecification.build(query, price, mrp), pageable)
                .map(
                        p -> abstractMapperService.toDto(p, ProductResponseDTO.class));

        System.out.println(page + "page in search");

        return page;
    }
      
    @Override
    public Page<ProductResponseDTO> getAllActiveProductPagination(Integer pageNo, Integer PageSize, String category) {

        Pageable pageable = PageRequest.of(pageNo, PageSize);

        // if cache hit then return from cache otherwise go to database and fetch data and return
        
        Page<ProductResponseDTO> productResponse=productCacheService.getResponse(pageNo,PageSize,category);
        
       if(productResponse != null){
       // log.debug("cache hit for pageNo={},PageSize={},category={}",pageNo,PageSize,category);
       // return;
       System.out.println("cache hit");
       return productResponse;
       }
       
        //cache is miss then call

        System.out.println("cache miss,querying database");

        Page<ProductResponseDTO> product=productRepository.findByIsActiveTrue(pageable).map(
            p->abstractMapperService.toDto(p,ProductResponseDTO.class)
        );

        System.out.println("caching product response");

        Page<ProductResponseDTO> products =
        productRepository.findByIsActiveTrue(pageable)
        .map(p -> abstractMapperService.toDto(p, ProductResponseDTO.class));

        productCacheService.cacheProductResponse(products,pageNo,PageSize,category);

        return product;
    }

    @Override
    public Page<ProductResponseDTO> getByArtisanId(Pageable pageable) {

        User user = authService.getCurrentUser();

        return productRepository.findByartId(user.getUserId(), pageable).map(
                p -> abstractMapperService.toDto(p, ProductResponseDTO.class));
    }

  //  @Cacheable(
        // value = "categoryProducts",
        // key = "#categoryId + '-' + #pageable.pageNumber + '-' + #pageable.pageSize"
    // )
    @Override
    public Page<ProductResponseDTO> getProductsByCategory(String category, Pageable pageable) {
        System.out.println("db call");

        return productRepository.findByCategory_Name(pageable, category).map(
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
