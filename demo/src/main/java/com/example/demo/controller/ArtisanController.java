package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.request.Artisan.ArtisanRequestDTO;
import com.example.demo.request.Product.ProductRequestDTO;
import com.example.demo.request.category.CategoryNameRequest;
import com.example.demo.response.Others.UpdateProduct;
import com.example.demo.response.Product.DeleteProductResponseDTO;
import com.example.demo.response.Product.ProductResponseDTO;
import com.example.demo.response.Product.ProductSaveResponse;
import com.example.demo.response.category.CategoryRequestResponseDTO;
import com.example.demo.service.methods.AartisanService;
import com.example.demo.service.methods.CategoryRequestService;
import com.example.demo.service.methods.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@PreAuthorize("hasRole('ARTISAN')")
@RequestMapping("/api/artisans")
public class ArtisanController {

    @Autowired
    private AartisanService aartisanService;

    @Autowired
    private CategoryRequestService categoryRequestService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ProductService productService;

    @PostMapping("/apply")
    public ResponseEntity<?> SaveArtisan(@RequestBody ArtisanRequestDTO artisanRequestDTO) {

        return ResponseEntity.ok(aartisanService.SaveArtisanDetails(artisanRequestDTO));

    }

    @GetMapping("/getArtisan")
    public ResponseEntity<?> getArtisanDetails(@RequestHeader("Authorization") String jwt) {

        return ResponseEntity.ok(aartisanService.getArtisanDetails(jwt));
    }

    @PostMapping("/updateArtisan")
    public ResponseEntity<?> UpdateArtisanDetails(@RequestBody ArtisanRequestDTO artisanRequestDTO,
            @RequestHeader("Authorization") String jwt) {

        return ResponseEntity.ok(aartisanService.UpdateArtisanDetails(artisanRequestDTO, jwt));

    }

    // request an category

    // POST /api/category-requests/create
    @PostMapping("/create")
    public ResponseEntity<CategoryRequestResponseDTO> createRequest(@RequestBody CategoryNameRequest dto) {
        return ResponseEntity.ok(categoryRequestService.createRequest(dto));
    }

    // GET /api/category-requests/my
    @GetMapping("/my")
    public ResponseEntity<List<CategoryRequestResponseDTO>> myRequests() {
        return ResponseEntity.ok(categoryRequestService.myRequests());
    }

    // product management

    @PostMapping(value = "/saveProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductSaveResponse> saveProduct(

            @RequestPart("product") String product,

            @RequestParam(required = true) List<MultipartFile> file

    )
            throws IOException {

        System.out.println(file.getClass().getName());
        System.out.println(product);

        if (product != null) {
            System.out.println("product json" + product);
        }

        if (file != null) {
            System.out.println("file" + file);
        }
        // convert string into product dto...
        ProductRequestDTO products = mapper.readValue(product, ProductRequestDTO.class);
        System.out.println(products + "product dto");
        System.out.println(products.getName() + "product name dto");
        return ResponseEntity.ok(productService.saveProducts(products, file));

    }

    // product is updated -by artian

    @PostMapping(value = "/UpdateProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UpdateProduct> UpdateProduct(@RequestPart("product") String product,

            @RequestParam Integer ProductId,

            @RequestPart("images") List<MultipartFile> files)
            throws IOException, Exception {

        ProductRequestDTO products = mapper.readValue(product, ProductRequestDTO.class);
        return ResponseEntity.ok(
                productService.updateProduct(products, ProductId, files));

    }

    // delete their own product => soft delete
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<DeleteProductResponseDTO> deleteProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(productService.DeactivateProduct(productId));
    }

    @GetMapping("/artisanId/product")
    public ResponseEntity<Page<ProductResponseDTO>> getProduct(@PathVariable Integer artisanId) {

        return ResponseEntity.ok(productService.getByArtisanId(artisanId));

    }

}
