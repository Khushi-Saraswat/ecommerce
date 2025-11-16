package com.example.demo.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.config.JwtService;
import com.example.demo.exception.AuthenticationIsNotValid;
import com.example.demo.exception.UserAlreadyExist;
import com.example.demo.model.Cart;
import com.example.demo.model.Feedback;
//import com.example.demo.model.Product;
import com.example.demo.model.ProductOrder;
import com.example.demo.model.RefreshToken;
import com.example.demo.model.UserDtls;
import com.example.demo.model.Wish;
import com.example.demo.repository.RefreshTokenRepo;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.AuthRequest;
import com.example.demo.request.RegRequest;
import com.example.demo.response.AuthResponse;
import com.example.demo.service.CartService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.FeedbackService;
import com.example.demo.service.OrderService;
import com.example.demo.service.RefreshTokenService;
import com.example.demo.service.UserInfoService;
import com.example.demo.service.UserService;
import com.example.demo.service.UserServiceImp;
import com.example.demo.service.WishService;
import com.example.demo.util.CommonUtil;
import com.example.demo.util.OrderStatus;

//import java.util.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private final RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private final RefreshTokenService refreshTokenService;

    @Autowired
    private UserServiceImp userServiceImp;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private WishService wishService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    UserController(RefreshTokenService refreshTokenService, RefreshTokenRepo refreshTokenRepo) {
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepo = refreshTokenRepo;
    }

    // create login endpoint- use to authenticate user if valid provide
    // authentication token.

    @PostMapping("/GenerateToken")
    public ResponseEntity<Map<String, String>> authenticationAndGetToken(@RequestBody AuthRequest authRequest) {

        AuthResponse authResponse = new AuthResponse();
        Authentication authentication;
        try {

            // authenticate() sends the username/password to the AuthenticationProvider,
            // which checks the raw password
            // against the stored hashed one. If they don't match, it throws an error.
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(), authRequest.getPassword()));
            System.out.println("authentication success: " + authentication);
        } catch (Exception e) {
            System.out.println("authentication failed: " + e.getMessage());
            return ResponseEntity.status(401).body(null);
        }

        UserDetails userDetails = userInfoService.loadUserByUsername(authRequest.getUsername());
        UserDtls userDtls = userRepository.findByusername(userDetails.getUsername());

        if (authentication.isAuthenticated()) {
            String jwt = jwtService.generateToken(userDetails.getUsername());
            authResponse.setJwt(jwt);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDtls.getId());
            System.out.println("jwt created");
            return ResponseEntity.ok(Map.of(
                    "accessToken", jwt,
                    "refreshToken", refreshToken.getToken()));

        }

        throw new AuthenticationIsNotValid("authentication is failed !!");

    }

    @PostMapping("/register")
    @ResponseBody
    public String register(@RequestBody RegRequest regrequest) {
        String name = regrequest.getName();
        String mobileNumber = regrequest.getMobileNumber();
        String username = regrequest.getUsername();
        String password = regrequest.getPassword();
        String role = regrequest.getRole();

        System.out.println(name + "" + mobileNumber + "" + username + "" + password + "" + "" + role);

        UserDtls userdtls = userRepository.findByusername(username);

        if (userdtls != null) {
            throw new UserAlreadyExist("UserName is already exist !!");
        }

        else {
            UserDtls userDtls = new UserDtls(name, mobileNumber, username, password, role);

            userRepository.save(userDtls);

            if (role.equals("USER"))
                userServiceImp.saveUser(userDtls);

            else {
                userServiceImp.saveUser(userDtls);
            }

            return "User added successfully!";
        }

    }

    @PostMapping("/refresh")
    @ResponseBody
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> payload) {

        String requestToken = payload.get("refreshToken");
        return refreshTokenRepo.findByToken(requestToken)
                .map(token -> {
                    if (refreshTokenService.isTokenExpired(token)) {
                        refreshTokenRepo.delete(token);
                        return ResponseEntity.badRequest().body("Refresh token expired. Please login again.");
                    }
                    String newJwt = jwtService.generateToken(token.getUser().getUsername());
                    return ResponseEntity.ok(Map.of("token", newJwt));
                })
                .orElse(ResponseEntity.badRequest().body("Invalid refresh token."));

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody Map<String, String> payload) {
        String requestToken = payload.get("refreshToken");

        if (requestToken == null || requestToken.isBlank()) {
            return ResponseEntity.badRequest().body("Refresh token is required");
        }

        return refreshTokenRepo.findByToken(requestToken)
                .map(token -> {
                    refreshTokenRepo.delete(token);
                    return ResponseEntity.ok("Logged out successfully");
                })
                .orElse(ResponseEntity.badRequest().body("Invalid refresh token"));
    }

    @GetMapping("/welcome")
    @ResponseBody
    public String welcome() {
        System.out.println("hi welcome");
        return "Welcome this endpoint is not secure";
    }

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
     * List<Category> allActiveCategory = categoryService.getAllActiveCategory();
     * m.addAttribute("category", allActiveCategory);
     * 
     * }
     */

    // this method is responsible for add product to cart
    @GetMapping("/addCart")
    public ResponseEntity<String> addToCart(@RequestParam Integer pid, @RequestParam Long uid) {
        System.out.println(pid + "" + uid + "in addToCart");
        Cart saveCart = cartService.saveCart(pid, uid);

        if (!ObjectUtils.isEmpty(saveCart)) {
            return new ResponseEntity<>("Product added to cart", HttpStatusCode.valueOf(200));
        }

        return new ResponseEntity<>("Product is not added successfully !!!", HttpStatus.NOT_MODIFIED);

    }

    @GetMapping("/api/cart")
    public ResponseEntity<List<Cart>> getCart(@RequestParam Long id) {
        List<Cart> cart = cartService.getCartByUsers(id);

        if (cart.size() > 0)
            return ResponseEntity.ok(cart);

        return new ResponseEntity<>(cart, HttpStatus.NOT_MODIFIED);

    }

    @DeleteMapping("/api/cart")
    public ResponseEntity<List<Cart>> getCart(@RequestParam Integer productid) {

    }

    /*
     * @GetMapping("/addWish")
     * public String addWish(@RequestParam Integer pid, @RequestParam Integer uid,
     * HttpSession session) {
     * System.out.println(pid + "" + uid);
     * Wish wish = wishService.saveWish(pid, uid);
     * if (ObjectUtils.isEmpty(wish)) {
     * session.setAttribute("errorMsg", "Product already added in wishlist");
     * } else {
     * session.setAttribute("succMsg", "Product added to wishlist");
     * }
     * return "redirect:/products/" + pid;
     * }
     */

    /*
     * @GetMapping("/cart")
     * public String loadCartPage(Principal p, Model m) {
     * UserDtls user = getLoggedInUserDetails(p);
     * List<Cart> carts = cartService.getCartByUsers(user.getId());
     * m.addAttribute("cart", carts);
     * if (carts.size() > 0) {
     * 
     * Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
     * m.addAttribute("totalOrderPrice", totalOrderPrice);
     * }
     * return "/user/cart";
     * }
     * 
     * @GetMapping("/wish")
     * public String loadWishPage(Principal p, Model m) {
     * UserDtls user = getLoggedInUserDetails(p);
     * List<Wish> Wish = wishService.getAllWishByIdUserId(user.getId());
     * System.out.println(Wish);
     * m.addAttribute("wish", Wish);
     * 
     * return "/user/Wishlist";
     * }
     */

    /*
     * private UserDtls getLoggedInUserDetails(Principal p) {
     * String email = p.getName();
     * UserDtls userDtls = userService.getUserByEmail(email);
     * return userDtls;
     * }
     */

    @GetMapping("/cartQuantityUpdate")
    public String cartQuantityUpdate(@RequestParam String sy, @RequestParam Integer cid) {
        cartService.updateQuantity(sy, cid);
        return "redirect:/user/cart";
    }

    /*
     * @GetMapping("/orders")
     * public String orderPage(Principal p, Model m) {
     * UserDtls user = getLoggedInUserDetails(p);
     * List<Cart> carts = cartService.getCartByUsers(user.getId());
     * m.addAttribute("cart", carts);
     * if (carts.size() > 0) {
     * 
     * Double orderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
     * Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice() +
     * 250 + 100;
     * m.addAttribute("totalOrderPrice", totalOrderPrice);
     * m.addAttribute("orderPrice", orderPrice);
     * }
     * return "/user/orders";
     * }
     */
    /*
     * @PostMapping("/save")
     * public String saveOrder(@ModelAttribute OrderRequest orderRequest, Principal
     * p) throws Exception {
     * System.out.println(orderRequest + "jsdg");
     * 
     * UserDtls userdtls = getLoggedInUserDetails(p);
     * orderService.saveOrder(userdtls.getId(), orderRequest);
     * 
     * return "/user/success";
     * }
     * 
     * @GetMapping("/user-orders")
     * public String myOrder(Model m, Principal p) {
     * UserDtls userdtls = getLoggedInUserDetails(p);
     * List<ProductOrder> orders = orderService.getOrdersByUser(userdtls.getId());
     * m.addAttribute("orders", orders);
     * return "/user/MyOrder";
     * }
     */

    @GetMapping("/update-status")
    public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, HttpSession session) {
        OrderStatus[] values = OrderStatus.values();
        String status = null;
        for (OrderStatus orderSt : values) {
            if (orderSt.getId().equals(st)) {
                System.out.println(orderSt.getId());
                status = orderSt.getName();
            }
        }
        ProductOrder updateOrder = orderService.updatOrderStatus(id, status);
        try {
            commonUtil.sendMailForProductOrder(updateOrder, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!ObjectUtils.isEmpty(updateOrder)) {
            session.setAttribute("succMsg", " Status Updated !!!");
        } else {
            session.setAttribute("errorMsg", "Status not updated!!!");
        }
        // System.out.println(values + "values");
        return "redirect:/user/user-orders";
    }

    @GetMapping("/profile")
    public String profile() {
        return "/user/profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute UserDtls user, @ModelAttribute MultipartFile img, HttpSession session) {
        UserDtls dbuser = userService.updateUserProfile(user, img);
        if (ObjectUtils.isEmpty(dbuser)) {
            session.setAttribute("errorMsg", "Profile not updated");
        } else {
            session.setAttribute("succMsg", "Profile Updated");
        }
        return "redirect:/user/profile";
    }

    /*
     * @PostMapping("/change-password")
     * public String changePassword(@RequestParam String newPassword, @RequestParam
     * String currentPassword, Principal p,
     * HttpSession session) {
     * UserDtls loggedInUserDetails = getLoggedInUserDetails(p);
     * 
     * boolean matches = passwordEncoder.matches(currentPassword,
     * loggedInUserDetails.getPassword());
     * 
     * if (matches) {
     * String encodePassword = passwordEncoder.encode(newPassword);
     * loggedInUserDetails.setPassword(encodePassword);
     * UserDtls updateUser = userService.updateUser(loggedInUserDetails);
     * if (ObjectUtils.isEmpty(updateUser)) {
     * session.setAttribute("errorMsg", "Password not updated !! Error in server");
     * } else {
     * session.setAttribute("succMsg", "Password Updated sucessfully");
     * }
     * } else {
     * session.setAttribute("errorMsg", "Current Password incorrect");
     * }
     * 
     * return "redirect:/user/profile";
     * }
     */

    @GetMapping("/delete/{id}")
    public String deleteWishProduct(@PathVariable int id, HttpSession session) {
        System.out.println("id for wishlist: " + id);
        Boolean delete = false;
        delete = wishService.deleteWishProduct(id);
        System.out.println("delete variable.." + delete);
        if (delete) {
            session.setAttribute("succMsg", "Product delete from wishlist");
        } else {
            session.setAttribute("errorMsg", "Product not delete from wishlist");
        }

        return "/user/Wishlist";
    }

    // @GetMapping("/add/{id}")
    // public String addToCart(@PathVariable int id, HttpSession session, Model m) {

    // System.out.println("id for addToCart: " + id);
    // Product product = wishService.GetProductByProductId(id);
    // m.addAttribute("cart", product);

    // return "/user/cart";
    // }

    @GetMapping("/sort")
    public String sortcart(@ModelAttribute Wish wish, Principal p) {
        // UserDtls user = getLoggedInUserDetails(p);
        // List<Product> products=wishService.getProductsByUserId(user.getId());
        // Collections.sort(pro);
        return "user/Wishlist";
    }

    @GetMapping("/filter")
    public String FilterWish(@ModelAttribute Wish wish, Principal p) {
        // UserDtls user = getLoggedInUserDetails(p);
        // List<Product> products=wishService.getProductsByUserId(user.getId());

        return "user/Wishlist";
    }

    @GetMapping("/feedback")
    public String feedback() {

        return "user/feedback";
    }

    @PostMapping("/saveFeedBack")
    public String saveFeed(@ModelAttribute Feedback feedback, HttpSession httpSession) {
        Boolean feed = false;
        feedback.setDate(LocalDateTime.now());
        feed = feedbackService.saveFeedBack(feedback);
        try {
            commonUtil.sendMailForFeedBack(feedback);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (feed) {
            httpSession.setAttribute("SuccMsg", "feedBack successfully saved");
        } else {
            httpSession.setAttribute("errorMsg", "feedBack is not successfully saved");
        }
        return "redirect:/user/feedback";

    }

}

// flow of spring security jwt
/*
 * Here’s the **most short + clear** version:
 ** 
 * Login phase:**
 * 
 * User sends username/password.
 * `AuthenticationManager` + `UserDetailsService` fetch user and verify password
 * using `PasswordEncoder`.
 * If correct → `JwtService` creates JWT and returns it.
 ** 
 * Secured request phase:**
 * 
 * User sends JWT in `Authorization` header.
 * `JwtAuthFilter` intercepts, extracts token, gets username from token,
 * validates token.
 * Loads user again using `UserDetailsService`.
 * Sets authenticated user in `SecurityContext`.
 ** 
 * Roles summary:**
 * 
 * **SecurityFilter / JwtAuthFilter:** Catches every request, reads/validates
 * JWT, sets auth context.
 * **JwtService:** Creates/validates/parses tokens.
 * **UserDetailsService:** Fetches user from DB.
 * **PasswordEncoder:** Checks raw vs hashed password.
 * **SecurityContext:** Stores who is logged in for that request.
 */