package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.EmailNotSend;
import com.example.demo.exception.InvalidEmail;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.UserDtls;
import com.example.demo.service.CartService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import com.example.demo.util.CommonUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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

    /*
     * @ModelAttribute
     * public void getUserDetails(Principal p, Model m) {
     * 
     * if (p != null) {
     * String email = p.getName();
     * UserDtls userDtls = userService.getUserByEmail(email);
     * m.addAttribute("user", userDtls);
     * Integer countCart = cartService.getCounterCart(userDtls.getId());
     * m.addAttribute("countCart", countCart);
     * 
     * }
     * 
     * List<Category> allActiveCategory = categoryService.getAllActiveCategory();
     * m.addAttribute("categoriess", allActiveCategory);
     * 
     * }
     */

    @GetMapping("/")
    public String index(Model m) {
        List<Category> allActiveCategory = categoryService.getAllActiveCategory().stream().limit(6).toList();
        List<Product> allactiveProducts = productService.getAllActiveProducts("").stream()
                .sorted((p1, p2) -> p2.getId().compareTo(p1.getId())).limit(8).toList();
        m.addAttribute("category", allActiveCategory);
        m.addAttribute("products", allactiveProducts);
        return "index";
    }

    @GetMapping("/signin")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String Register() {
        return "register";
    }

    @GetMapping("/product")
    public String product(Model m, @RequestParam(value = "category", defaultValue = "") String category,
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "9") Integer pageSize) {
        System.out.println("categories" + category);
        List<Category> categories = categoryService.getAllActiveCategory();

        m.addAttribute("categories", categories);
        m.addAttribute("paramvalue", category);
        // List<Product> products = productService.getAllActiveProducts(category);
        // m.addAttribute("products", products);

        Page<Product> page = productService.getAllActiveProductPagination(pageNo, pageSize, category);
        List<Product> product = page.getContent();
        m.addAttribute("products", product);
        m.addAttribute("productSize", product.size());
        System.out.println(product.size() + "product size");
        m.addAttribute("pageNo", page.getNumber());
        System.out.println(page.getNumber() + "pagenumber");
        m.addAttribute("pageSize", pageSize);
        System.out.println(pageSize + "pageSize");
        m.addAttribute("totalElements", page.getTotalElements());
        System.out.println("totalElements" + page.getTotalElements());
        m.addAttribute("totalPages", page.getTotalPages());
        System.out.println("totalPages" + page.getTotalElements());
        m.addAttribute("isFirst", page.isFirst());
        System.out.println("isFirst" + page.isFirst());
        m.addAttribute("isLast", page.isLast());
        System.out.println("isLast" + page.isLast());
        return "product";
    }

    @GetMapping("/products/{id}")
    public String products(@PathVariable int id, Model m) {
        Product productById = productService.getProductById(id);
        m.addAttribute("product", productById);
        return "viewproduct";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute UserDtls user, @RequestParam("img") MultipartFile file,
            HttpSession httpSession) {
        String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setProfileimage(imageName);
        UserDtls saveUser = userService.saveUser(user);
        if (!ObjectUtils.isEmpty(saveUser)) {
            if (!file.isEmpty()) {
                try (InputStream inputStream = file.getInputStream()) {
                    Path path = Paths.get("C:\\Users\\DELL\\Desktop\\ecommerce\\demo\\src\\main\\resources\\static\\img"
                            + File.separator + "profile_img" + File.separator
                            + file.getOriginalFilename());

                    System.out.println(path);
                    Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                httpSession.setAttribute("succMsg", "saved successfully");
            }
        } else {
            httpSession.setAttribute("errorMsg", "something wrong on server");
        }
        return "redirect:/register";

    }

    // forgot password logic..
    /*
     * * @GetMapping("/forgot-password")
     * public String showForgotPassword() {
     * return "forgotPassword";
     * }
     */

    // forgot password logic..

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
    /*
     * @GetMapping("/reset-password")
     * public String ShowResetPassword(@RequestParam String token,
     * Model m) {
     * System.out.println(token + "token in home");
     * UserDtls userByToken = userService.getUserByToken(token);
     * if (userByToken == null) {
     * m.addAttribute("errorMsg", "Your link is invalid or expired !!");
     * return "message";
     * }
     * m.addAttribute("token", token);
     * return "reset_password";
     * }
     */

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

    @GetMapping("/search")
    public String searchProduct(@RequestParam String ch, Model m) {
        List<Product> searchProduct = productService.searchProduct(ch);
        m.addAttribute("products", searchProduct);
        List<Category> categories = categoryService.getAllActiveCategory();
        m.addAttribute("categories", categories);

        return "product";
    }

}
