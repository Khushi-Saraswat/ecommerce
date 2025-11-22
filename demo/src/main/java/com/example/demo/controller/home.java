package com.example.demo.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.exception.EmailNotSend;
import com.example.demo.exception.InvalidEmail;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.UserDtls;
import com.example.demo.response.ProductCat;
import com.example.demo.service.methods.CartService;
import com.example.demo.service.methods.CategoryService;
import com.example.demo.service.methods.ProductService;
import com.example.demo.service.methods.UserService;
import com.example.demo.util.CommonUtil;
import com.mysql.cj.util.StringUtils;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

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

    /*
     * @PostMapping("/saveUser")
     * public String saveUser(@ModelAttribute UserDtls user, @RequestParam("img")
     * MultipartFile file,
     * HttpSession httpSession) {
     * String imageName = file.isEmpty() ? "default.jpg" :
     * file.getOriginalFilename();
     * user.setProfileimage(imageName);
     * UserDtls saveUser = userService.saveUser(user);
     * if (!ObjectUtils.isEmpty(saveUser)) {
     * if (!file.isEmpty()) {
     * try (InputStream inputStream = file.getInputStream()) {
     * Path path = Paths.get(
     * "C:\\Users\\DELL\\Desktop\\ecommerce\\demo\\src\\main\\resources\\static\\img"
     * + File.separator + "profile_img" + File.separator
     * + file.getOriginalFilename());
     * 
     * System.out.println(path);
     * Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
     * } catch (IOException ioe) {
     * ioe.printStackTrace();
     * }
     * httpSession.setAttribute("succMsg", "saved successfully");
     * }
     * } else {
     * httpSession.setAttribute("errorMsg", "something wrong on server");
     * }
     * return "redirect:/register";
     * 
     * }
     */

    @PostMapping("/forgot-password")
    public ResponseEntity<String> processForgotPassword(@RequestParam String username, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {

        // find user using entered email
        UserDtls userByEmail = userService.getUserByEmail(username);

        // if user is not found show error invalid email..
        if (ObjectUtils.isEmpty(userByEmail)) {
            throw new InvalidEmail("Invalid email !!!!");
        }
        // if user is found create token and add it to the end of generated email and
        // send email via passing url and email.
        else {

            String resetToken = UUID.randomUUID().toString();
            System.out.println(resetToken + "resetToken");
            userService.updateUserResetToken(username, resetToken);
            // generate-url-http://localhost:8080/reset-password?tokendhegffffffff
            String url = commonUtil.generateUrl(request) + "/reset-password?token=" +
                    resetToken;
            System.out.println(url + "url");
            Boolean sendMail = commonUtil.sendMail(url, username);
            if (sendMail) {

                return ResponseEntity.status(HttpStatus.CREATED).body("please check the email password link is send");
            } else {

                throw new EmailNotSend("Something wrong on server ! Email not send");
            }

        }

    }

    // reset password logic..
    @PostMapping("/reset-password")
    public ResponseEntity<String> ResetPassword(@RequestParam String token, @RequestParam String password) {

        // using that token i find the user if token is null link is expired and if not
        // set correct password and save the user
        UserDtls userByToken = userService.getUserByToken(token);
        if (userByToken == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The password reset link is invalid or expired.");

        } else {
            userByToken.setPassword(passwordEncoder.encode(password));
            userByToken.setResetToken(null);
            userService.updateUser(userByToken);
            return ResponseEntity.status(HttpStatus.CREATED).body("password is set correctly");
        }

    }

    // search a particular product using keyword.
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String ch) {
        // get list of search product ....
        List<Product> searchProduct = productService.searchProduct(ch);
        return ResponseEntity.ok(searchProduct);
    }

}
