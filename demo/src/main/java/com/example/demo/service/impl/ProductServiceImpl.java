package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.service.methods.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	/*
	 * private final UserRepository userRepository;
	 * 
	 * @Autowired
	 * private AbstractMapperService abstractMapperService;
	 * 
	 * @Autowired
	 * private PriceHistoryRepo priceHistoryRepo;
	 * 
	 * @Autowired
	 * private UserService userService;
	 * 
	 * @Autowired
	 * private ProductRepository productRepository;
	 * 
	 * @Autowired
	 * private ArtisanRepository artisanRepository;
	 * 
	 * @Autowired
	 * private CloudinaryService cloudinaryService;
	 * 
	 * @Autowired
	 * private AuthService authService;
	 * 
	 * @Autowired
	 * private CategoryRepository categoryRepository;
	 * 
	 * ProductServiceImpl(UserRepository userRepository) {
	 * this.userRepository = userRepository;
	 * }
	 * 
	 * // this method is used to save a product in database
	 * public ProductSaveResponse saveProducts(ProductRequestDTO product,
	 * List<MultipartFile> file) throws IOException {
	 * 
	 * Product savedProduct = null;
	 * 
	 * // Authenticate user
	 * User user = authService.getCurrentUser();
	 * 
	 * if (user == null) {
	 * throw new AuthException("jwt token is missing", AuthErrorType.TOKEN_MISSING);
	 * }
	 * 
	 * Product productEntity = abstractMapperService.toEntity(product,
	 * Product.class);
	 * 
	 * 
	 * Optional<Category> optionalCategory =
	 * categoryRepository.findById(product.getCategoryId());
	 * 
	 * if (optionalCategory.isEmpty()) {
	 * ProductSaveResponse responseDTO = new ProductSaveResponse();
	 * responseDTO.setSuccess(false);
	 * responseDTO.setConfigurationMessage(
	 * "Category not found. Please request admin to add category"
	 * );
	 * return responseDTO;
	 * }
	 * 
	 * productEntity.setCategoryId(productEntity.getCategoryId());
	 * 
	 * 
	 * productEntity.setCreatedAt(LocalDateTime.now());
	 * 
	 * // productEntity.setArtisan();
	 * // Auto-id artisan id..
	 * 
	 * // Resolve and assign Artisan entity for the authenticated user
	 * Artisan artisan = artisanRepository
	 * .findByUserUserId(user.getUserId())
	 * .orElseThrow(() -> new UserException("artisan profile not found",
	 * UserErrorType.NOT_FOUND));
	 * 
	 * productEntity.setArtisan(artisan);
	 * productEntity.setArtId(artisan.getId().intValue());
	 * 
	 * // find category by name......
	 * 
	 * // productEntity.setArtisan(productEntity.getArtisan().get);
	 * // productEntity.setCategory(product.getCa)
	 * 
	 * System.out.println("product name:" + productEntity.getName());
	 * // 2️⃣ Slug
	 * String slug = productEntity.getName()
	 * .toLowerCase()
	 * .replaceAll("[^a-z0-9]+", "-");
	 * productEntity.setSlug(slug);
	 * 
	 * List<ProductImage> image = new ArrayList<>();
	 * 
	 * for (MultipartFile images : file) {
	 * String imageUrl = cloudinaryService.uploadImage(images);
	 * ProductImage productImage = new ProductImage();
	 * productImage.setImageUrl(imageUrl);
	 * productImage.setProduct(productEntity);
	 * image.add(productImage);
	 * }
	 * 
	 * productEntity.setImages(image);
	 * 
	 * // 5️⃣ Save
	 * if (artisan.getKycStatus().equals(KycStatus.APPROVED)) {
	 * savedProduct = productRepository.save(productEntity);
	 * }
	 * 
	 * else {
	 * throw new UserException("Artisan is not approved",
	 * UserErrorType.NOT_APPROVED);
	 * }
	 * 
	 * if (savedProduct == null) {
	 * throw new ProductException("Product is not saved",
	 * ProductErrorType.PRODUCT_NOT_SAVED);
	 * }
	 * 
	 * ProductSaveResponse responseDTO = new ProductSaveResponse();
	 * responseDTO.setArtisanId(savedProduct.getArtId());
	 * responseDTO.setProductId(savedProduct.getId());
	 * responseDTO.setConfigurationMessage("Product saved successfully");
	 * return responseDTO;
	 * 
	 * }
	 * 
	 * // this method is responsible for returning all products ...
	 * 
	 * @Override
	 * public List<Products> getAllProducts() {
	 * List<Product> product = productRepository.findAll();
	 * 
	 * for (Product p : product) {
	 * List<ProductImage> images =
	 * productRepository.findProductImagesById(p.getId());
	 * for (ProductImage img : images) {
	 * String imageUrls = cloudinaryService.getImageUrl(img.getId().toString());
	 * System.out.println("image url:" + img.getImageUrl());
	 * img.setImageUrl(imageUrls);
	 * p.getImages().add(img);
	 * 
	 * }
	 * }
	 * return product.stream().map(
	 * prod -> {
	 * return abstractMapperService.toDto(prod, Products.class);
	 * 
	 * }
	 * 
	 * ).toList();
	 * }
	 * 
	 * // this method is responsible for getting a product by id.
	 * 
	 * @Transactional
	 * 
	 * @Override
	 * /* public Products getProductById(Integer id) {
	 * Product product = productRepository.findById(id).orElse(null);
	 * 
	 * List<ProductImage> img = product.getImages();
	 * for (ProductImage images : img) {
	 * String imageUrls = cloudinaryService.getImageUrl(images.getId().toString());
	 * images.setImageUrl(imageUrls);
	 * }
	 * 
	 * product.setImages(img);
	 * 
	 * return abstractMapperService.toDto(product, Products.class);
	 * }
	 * 
	 * // this method is responsible for getting all the active products.
	 * /*
	 * 
	 * @Override
	 * public List<Product> getAllActiveProducts(CategoryDto category) {
	 * List<Product> products = null;
	 * if (ObjectUtils.isEmpty(category)) {
	 * products = productRepository.findByIsActiveTrue();
	 * } else {
	 * // products = productRepository
	 * // .findByCategory((Category) abstractMapperService.toEntity(products));
	 * }
	 * 
	 * return products;
	 * }
	 */

	/*
	 * @Override
	 * public List<ProductDto> searchProduct(String ch) {
	 * 
	 * // The title has the keyword OR the category has the keyword and it doesn't
	 * // matter if the text is uppercase or lowercase.
	 * List<ProductDto> productDto = productRepository
	 * .findByTitleOrCategoryNameContainingIgnoreCase(ch);
	 * 
	 * return productDto.stream()
	 * .map(product -> (ProductDto)
	 * abstractMapperService.convertEntityToDto(product))
	 * .collect(Collectors.toList());
	 * 
	 * }
	 */

	// same above logic for searching the product but along with pagination
	/*
	 * @Override
	 * public Page<Product> searchProductPagination(Integer pageNo, Integer
	 * pageSize, String ch) {
	 * Pageable pageable = PageRequest.of(pageNo, pageSize);
	 * return productRepository.findByTitleOrCategoryNameContainingIgnoreCase(ch,
	 * pageable);
	 * 
	 * }
	 */

	// get all the active products by admin but with pagination.
	// @Override
	// public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer
	// PageSize, String category) {
	// Pageable pageable = PageRequest.of(pageNo, PageSize);
	// Page<Product> pageProduct = null;
	/*
	 * if (ObjectUtils.isEmpty(category)) {
	 * 
	 * pageProduct = productRepository.findByIsActiveTrue(pageable);
	 * } else {
	 * pageProduct = productRepository.findByCategory(pageable, category);
	 * }
	 * return pageProduct;
	 * }
	 * 
	 * // get all products with pagination
	 * 
	 * @Transactional
	 * 
	 * @Override
	 * public Page<Product> getAllProducts(Integer pageNo, Integer pageSize) {
	 * Pageable pageable = PageRequest.of(pageNo, pageSize);
	 * return productRepository.findAll(pageable);
	 * }
	 * 
	 * @Transactional
	 * 
	 * @Override
	 * public String IncreaseStock(Integer productId, Integer stockval) {
	 * Products product = getProductById(productId);
	 * 
	 * Product pro = abstractMapperService.toEntity(product,
	 * Product.class);
	 * System.out.println("stock in products:" + pro);
	 * System.out.println("product id in products table:" + pro.getId());
	 * 
	 * if (!pro.getIsActive()) {
	 * throw new RuntimeException("Product is deactivate");
	 * }
	 * 
	 * if (pro.getStock() > 0) {
	 * pro.setStock(stockval);
	 * System.out.println("stock value in products:" + "" + pro.getStock());
	 * System.out.println("product after stock:" + pro);
	 * // pro.setQuantity("IN_STOCK");
	 * return "Stock updated";
	 * 
	 * }
	 * 
	 * // add here product out of stock..
	 * // pro.setQuantity("OUT_OF_STOCK");
	 * return "Stock not updated";
	 * 
	 * }
	 * 
	 * @Override
	 * public UpdateProduct updateProduct(ProductRequestDTO dto, Integer productId,
	 * List<MultipartFile> files)
	 * throws IOException {
	 * 
	 * // 1️⃣ Authenticate user
	 * User user = authService.getCurrentUser();
	 * 
	 * if (user == null) {
	 * throw new AuthException("jwt token is missing", AuthErrorType.TOKEN_MISSING);
	 * }
	 * 
	 * // 2️⃣ Fetch artisan of logged-in user
	 * Artisan artisan = artisanRepository
	 * .findByUserUserId(user.getUserId())
	 * .orElseThrow(() -> new UserException("artisan profile not found",
	 * UserErrorType.NOT_FOUND));
	 * 
	 * // Optional: KYC check
	 * if (!artisan.getKycStatus().equals(KycStatus.APPROVED)) {
	 * throw new UserException("Artisan is not approved",
	 * UserErrorType.NOT_APPROVED);
	 * }
	 * 
	 * // 3️⃣ Fetch existing product
	 * Product existingProduct = productRepository.findById(productId)
	 * .orElseThrow(() -> new ProductException("Product not found",
	 * ProductErrorType.PRODUCT_NOT_FOUND));
	 * 
	 * // 4️⃣ Ensure product belongs to logged-in artisan
	 * if (existingProduct.getArtisan() == null ||
	 * !existingProduct.getArtisan().getId().equals(artisan.getId())) {
	 * throw new UserException("You are not authorized to update this product",
	 * UserErrorType.UNAUTHORIZED);
	 * }
	 * 
	 * // 5️⃣ Check active status
	 * if (!Boolean.TRUE.equals(existingProduct.getIsActive())) {
	 * throw new ProductException("Product is inactive",
	 * ProductErrorType.PRODUCT_INACTIVE);
	 * }
	 * 
	 * // 6️⃣ CATEGORY VALIDATION (IMPORTANT)
	 * if (dto.getCategoryId() != null) {
	 * 
	 * Category category = categoryRepository.findById(dto.getCategoryId())
	 * .orElseThrow(() -> new CategoryException(
	 * "Category not found. Please request admin using /api/category-requests/create"
	 * ,
	 * CategoryError.CATEGORY_NOT_FOUND
	 * ));
	 * 
	 * existingProduct.setCategoryId(dto.getCategoryId());
	 * }
	 * 
	 * // 7️⃣ Price history (store old price before update)
	 * Double oldPrice = existingProduct.getPrice();
	 * 
	 * // 8️⃣ Update basic fields
	 * existingProduct.setName(dto.getName());
	 * existingProduct.setDescription(dto.getDescription());
	 * existingProduct.setPrice(dto.getPrice());
	 * existingProduct.setMrp(dto.getMrp());
	 * existingProduct.setStock(dto.getStock());
	 * 
	 * // 9️⃣ Slug update
	 * String slug = dto.getName()
	 * .toLowerCase()
	 * .replaceAll("[^a-z0-9]+", "-");
	 * existingProduct.setSlug(slug);
	 * 
	 * // 🔟 Update price history if changed
	 * if (oldPrice != null && dto.getPrice() != null &&
	 * !oldPrice.equals(dto.getPrice())) {
	 * PriceHistory history = new PriceHistory();
	 * history.setOldPrice(oldPrice);
	 * history.setNewPrice(dto.getPrice());
	 * history.setChangedAt(LocalDateTime.now());
	 * history.setProduct(existingProduct);
	 * priceHistoryRepo.save(history);
	 * }
	 * 
	 * // 1️⃣1️⃣ Images update
	 * if (files != null && !files.isEmpty()) {
	 * 
	 * // Delete old images
	 * List<ProductImage> existingImages =
	 * productRepository.findProductImagesById(existingProduct.getId());
	 * 
	 * for (ProductImage img : existingImages) {
	 * // NOTE: ideally delete using publicId, not DB id
	 * cloudinaryService.deleteImage(img.getId().toString());
	 * }
	 * 
	 * // Upload new images
	 * List<ProductImage> newImages = new ArrayList<>();
	 * 
	 * for (MultipartFile file : files) {
	 * String imageUrl = cloudinaryService.uploadImage(file);
	 * 
	 * ProductImage productImage = new ProductImage();
	 * productImage.setImageUrl(imageUrl);
	 * productImage.setProduct(existingProduct);
	 * 
	 * newImages.add(productImage);
	 * }
	 * 
	 * existingProduct.setImages(newImages);
	 * }
	 * 
	 * // 1️⃣2️⃣ Save
	 * Product saved = productRepository.save(existingProduct);
	 * 
	 * // 1️⃣3️⃣ Return DTO
	 * return abstractMapperService.toDto(saved, UpdateProduct.class);
	 * }
	 * 
	 * 
	 * @Override
	 * public Boolean DeactivateProduct(int ProductId) {
	 * Product product = productRepository.findById(ProductId).orElse(null);
	 * 
	 * if (!ObjectUtils.isEmpty(product)) {
	 * product.setIsActive(false);
	 * productRepository.save(product);
	 * return true;
	 * }
	 * 
	 * return false;
	 * }
	 * 
	 * @Override
	 * public Boolean ActivateProduct(Integer productId) {
	 * Product product =
	 * productRepository.findById(productId.intValue()).orElse(null);
	 * 
	 * if (!ObjectUtils.isEmpty(product)) {
	 * product.setIsActive(true);
	 * productRepository.save(product);
	 * return true;
	 * }
	 * 
	 * return false;
	 * }
	 * 
	 * @Override
	 * public StockResponse StockDetails(Integer productId) {
	 * 
	 * Product existingProduct = productRepository.findById(productId.intValue())
	 * .orElseThrow(() -> new RuntimeException("Product not found"));
	 * 
	 * if (!existingProduct.getIsActive()) {
	 * throw new RuntimeException("Product is inactive");
	 * }
	 * 
	 * StockResponse response = new StockResponse();
	 * response.setProductId(productId.toString());
	 * response.setStock(existingProduct.getStock());
	 * // response.setStatus(existingProduct.getQuantity());
	 * 
	 * return response;
	 * }
	 * 
	 * @Transactional
	 * 
	 * @Override
	 * public List<ProductResponseDTO> getByArtisanId(String jwt) {
	 * 
	 * User user = userService.getUserByJwt(jwt);
	 * if (user == null) {
	 * throw new AuthException("jwt token is missing", AuthErrorType.TOKEN_MISSING);
	 * }
	 * 
	 * // Ensure role
	 * if (user.getRole() == null ||
	 * !user.getRole().equals(com.example.demo.constants.Role.ARTISAN)) {
	 * throw new
	 * UserException("Only artisan is responsible for accessing the added products",
	 * UserErrorType.UNAUTHORIZED);
	 * }
	 * 
	 * User users = userRepository.findByUserId(user.getUserId());
	 * 
	 * List<Product> pro =
	 * productRepository.findByartId(users.getUserId().intValue());
	 * 
	 * 
	 * // fetch products images as well
	 * 
	 * /* for (Product p : pro) {
	 * List<ProductImage> images =
	 * productRepository.findProductImagesById(p.getId());
	 * for (ProductImage img : images) {
	 * String imageUrls = cloudinaryService.getImageUrl(img.getId().toString());
	 * System.out.println("image url:" + img.getImageUrl());
	 * img.setImageUrl(imageUrls);
	 * p.getImages().add(img);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * }
	 */

	/*
	 * return pro.stream().map(
	 * prod -> {
	 * return abstractMapperService.toDto(prod, ProductResponseDto.class);
	 * 
	 * }
	 * 
	 * ).toList();
	 * }
	 * 
	 * // Local discovery: priority filters -> city, state, fallback to all active
	 * 
	 * @Override
	 * public List<Products> localDiscovery(String city, String state) {
	 * 
	 * List<Product> products = null;
	 * 
	 * // Try city-level filter first
	 * if (!ObjectUtils.isEmpty(city)) {
	 * products =
	 * productRepository.findByArtisanCityIgnoreCaseAndIsActiveTrue(city);
	 * if (products != null && !products.isEmpty()) {
	 * return products.stream()
	 * .map(prod -> abstractMapperService.toDto(prod, Products.class))
	 * .toList();
	 * }
	 * }
	 * 
	 * // Fallback to state-level filter
	 * if (!ObjectUtils.isEmpty(state)) {
	 * products =
	 * productRepository.findByArtisanStateIgnoreCaseAndIsActiveTrue(state);
	 * if (products != null && !products.isEmpty()) {
	 * return products.stream()
	 * .map(prod -> abstractMapperService.toDto(prod, Products.class))
	 * .toList();
	 * }
	 * }
	 * 
	 * // Final fallback: all active products
	 * products = productRepository.findByIsActiveTrue();
	 * 
	 * return products.stream()
	 * .map(prod -> abstractMapperService.toDto(prod, Products.class))
	 * .toList();
	 * 
	 * }
	 * 
	 * }
	 */
}

/*
 * Real-World Example to Understand
 * 
 * Imagine your product table:
 * 
 * ID Title Category
 * 1 Blue Shirt Clothing
 * 2 Laptop Bag Accessories
 * 3 T-Shirt Clothing
 * 
 * Now you call:
 * 
 * findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase("shirt",
 * "shirt")
 * 
 * 
 * What happens?
 * 
 * Check Title:
 * 
 * "Blue Shirt" → contains "shirt" → MATCH
 * 
 * "Laptop Bag" → does not contain → NO
 * 
 * "T-Shirt" → contains → MATCH
 * 
 * Check Category:
 * 
 * "Clothing" → no
 * 
 * "Accessories" → no
 * 
 * "Clothing" → no
 * 
 * Results returned:
 * 
 * Blue Shirt
 * 
 * T-Shirt
 */