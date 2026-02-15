package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.methods.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper mapper;

    // this method is responsible for saving the product of a particular category-by
    // artian

    /*
     * @PostMapping(value = "/saveProduct", consumes =
     * MediaType.MULTIPART_FORM_DATA_VALUE)
     * public ResponseEntity<ProductSaveResponse> saveProduct(
     * 
     * @RequestPart("product") String product,
     * 
     * @RequestParam(required = true) List<MultipartFile> file
     * 
     * )
     * throws IOException {
     * 
     * System.out.println(file.getClass().getName());
     * System.out.println(product);
     * 
     * if (product != null) {
     * System.out.println("product json" + product);
     * }
     * 
     * if (file != null) {
     * System.out.println("file" + file);
     * }
     * // convert string into product dto...
     * ProductRequestDto products = mapper.readValue(product, Prod.class);
     * System.out.println(products + "product dto");
     * System.out.println(products.getName() + "product name dto");
     * return ResponseEntity.ok(productService.saveProducts(products, file));
     * 
     * }
     * 
     * // product is updated -by artian
     * 
     * @PostMapping(value = "/UpdateProduct", consumes =
     * MediaType.MULTIPART_FORM_DATA_VALUE)
     * public ResponseEntity<UpdateProduct> UpdateProduct(@RequestPart("product")
     * String product,
     * 
     * @RequestParam Integer ProductId,
     * 
     * @RequestPart("images") List<MultipartFile> files)
     * throws IOException, Exception {
     * 
     * Products products = mapper.readValue(product, Products.class);
     * return ResponseEntity.ok(
     * productService.updateProduct(products, ProductId, files));
     * 
     * }
     * 
     * 
     * 
     * @GetMapping("/getArtisanProduct")
     * public ResponseEntity<?> GetArtisanProduct(
     * 
     * @RequestHeader("Authorization") String jwt)
     * throws IOException {
     * 
     * return ResponseEntity.ok(productService.getByArtisanId(jwt));
     * 
     * }
     * 
     * // stock is updated-by admin
     * 
     * @PatchMapping("/StockUpdate/stock")
     * public ResponseEntity<String> UpdateStock(@RequestParam Integer
     * ProductId, @RequestParam Integer stock) {
     * String message = productService.IncreaseStock(ProductId, stock);
     * return ResponseEntity.ok(message);
     * }
     * 
     * // get all products by user
     * // @GetMapping("/allproduct")
     * // public ResponseEntity<List<Products>> ViewProduct() {
     * 
     * // return ResponseEntity.ok(productService.getAllProducts());
     * 
     * // }
     * 
     * // get products by product id-used by both admin+user
     * //@GetMapping("/getProduct")
     * // public ResponseEntity<?> ProductDetails(@RequestParam Integer id) {
     * 
     * // return ResponseEntity.ok(productService.getProductById(id));
     * 
     * //}
     * 
     * // pass product-id
     * // @GetMapping("/stock")
     * // public ResponseEntity<StockResponse> StockDetails(@RequestParam Integer
     * id) {
     * 
     * // StockResponse response = productService.StockDetails(id);
     * 
     * // return ResponseEntity.ok(response);
     * 
     * // }
     * 
     * // search product by user
     * 
     * /*
     * 
     * @GetMapping("/searchProduct")
     * public ResponseEntity<?> SearchProduct(@RequestParam String ProductName) {
     * 
     * List<ProductDto> productDto = productService.searchProduct(ProductName);
     * 
     * if (productDto == null) {
     * return ResponseEntity.status(404).body("Product not found");
     * }
     * 
     * // return product
     * return ResponseEntity.ok(productDto);
     * }
     */

    // Local discovery endpoint: city has highest priority, then state, then all
    // @GetMapping("/local")
    // public ResponseEntity<List<Products>> localDiscovery(
    // @RequestParam(required = false) String city,
    // @RequestParam(required = false) String state) {

    // return ResponseEntity.ok(productService.localDiscovery(city, state));
    // }

    // this method all the active product and category...
    /*
     * @GetMapping("/allProductsCategory")
     * public ResponseEntity<?> index(CategoryDto category) {
     * List<Category> allActiveCategory =
     * categoryService.getAllActiveCategory().stream().limit(6).toList();
     * // apply stream 8 logic..
     * List<Product> allactiveProducts =
     * productService.getAllActiveProducts(category).stream()
     * .sorted((p1, p2) -> p2.getId().compareTo(p1.getId())).limit(8).toList();
     * 
     * ProductCat productCat = new ProductCat();
     * productCat.setCategorie(allActiveCategory);
     * productCat.setProduct(allactiveProducts);
     * 
     * if (allactiveProducts == null || allactiveProducts.isEmpty()) {
     * return ResponseEntity.status(HttpStatus.NOT_FOUND)
     * .body(Map.of("message", "No products present"));
     * }
     * return ResponseEntity.ok(productCat);
     * }
     */

    /*
     * @PutMapping("/artisan/{productId}/active")
     * public ResponseEntity<String> activateProduct(@PathVariable Integer
     * productId){
     * 
     * return ResponseEntity.ok(productService.ActivateProduct(productId));
     * 
     * }
     * 
     * 
     * @PutMapping("/artisan/{productId}/inactive")
     * public ResponseEntity<String> deactivateProduct(@PathVariable Integer
     * productId){
     * 
     * return ResponseEntity.ok(productService.DeactivateProduct(productId));
     * }
     */

}
