package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.response.ProductCat;
import com.example.demo.service.methods.CartService;
import com.example.demo.service.methods.CategoryService;
import com.example.demo.service.methods.ProductService;
import com.example.demo.service.methods.UserService;
import com.example.demo.util.CommonUtil;
import com.mysql.cj.util.StringUtils;

@Controller
public class home {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private CartService cartService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // this method all the active product and category...
    @GetMapping("/")
    public ResponseEntity<ProductCat> index(Model m) {
        List<Category> allActiveCategory = categoryService.getAllActiveCategory().stream().limit(6).toList();
        // apply stream 8 logic..
        List<Product> allactiveProducts = productService.getAllActiveProducts("").stream()
                .sorted((p1, p2) -> p2.getId().compareTo(p1.getId())).limit(8).toList();

        ProductCat productCat = new ProductCat();
        productCat.setCategorie(allActiveCategory);
        productCat.setProduct(allactiveProducts);

        if (productCat != null)
            return ResponseEntity.ok(productCat);

        return null;
    }

    // this method require category for pagination (pageno and pagesize) and then it
    // return List
    // of product
    // responsible for fetching product by search category+search product
    @GetMapping("/product")
    public ResponseEntity<List<Product>> product(@RequestParam(value = "category", defaultValue = "") String category,
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "9") Integer pageSize,
            @RequestParam(defaultValue = "") String ch) {
        // first of all find all categories and then find products for that particular
        // category and
        // by means of pageno,pagesize we got list of products.

        System.out.println("categories" + category);
        Page<Product> page = null;
        if (StringUtils.isNullOrEmpty(ch)) {
            // if the search product is null then call all active product by pagination.
            page = productService.getAllActiveProductPagination(pageNo, pageSize, category);
        }

        else {
            // if the search product is not null then search that particular product with
            // pagination.
            page = productService.searchProductPagination(pageNo, pageSize, ch);
        }

        List<Product> product = page.getContent();

        return ResponseEntity.ok(product);
    }

    // this method is responsible for getting products by id..
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> products(@PathVariable int id) {
        // find product by a particular id.
        Product productById = productService.getProductById(id);
        return ResponseEntity.ok(productById);
    }

    // search a particular product using keyword.
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String ch) {
        // get list of search product ....
        List<Product> searchProduct = productService.searchProduct(ch);
        return ResponseEntity.ok(searchProduct);
    }

}
