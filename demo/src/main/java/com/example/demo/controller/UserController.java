package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
import com.example.demo.dto.FeedbackDto;
import com.example.demo.model.Cart;
//import com.example.demo.model.Product;
import com.example.demo.model.ProductOrder;
import com.example.demo.model.UserDtls;
import com.example.demo.model.Wish;
import com.example.demo.repository.RefreshTokenRepo;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.RefreshTokenService;
import com.example.demo.service.impl.UserInfoService;
import com.example.demo.service.impl.UserServiceImp;
import com.example.demo.service.methods.CartService;
import com.example.demo.service.methods.CategoryService;
import com.example.demo.service.methods.FeedbackService;
import com.example.demo.service.methods.OrderService;
import com.example.demo.service.methods.UserService;
import com.example.demo.service.methods.WishService;
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

    @GetMapping("/welcome")
    @ResponseBody
    public String welcome() {
        System.out.println("hi welcome");
        return "Welcome this endpoint is not secure";
    }

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

    // it is responsible for getting a cart by id.
    @GetMapping("/api/cart")
    public ResponseEntity<List<Cart>> getCart(@RequestParam Long id) {
        List<Cart> cart = cartService.getCartByUsers(id);

        if (cart.size() > 0)
            return ResponseEntity.ok(cart);

        return new ResponseEntity<>(cart, HttpStatus.NOT_MODIFIED);

    }

    // this method is responsible for deleting a product by id in cart
    @DeleteMapping("/api/cart")
    public ResponseEntity<String> DeleteCart(@RequestParam Integer productid) {
        Boolean cartDelete = cartService.deleteCart(productid);
        if (cartDelete)
            return ResponseEntity.ok("successfully cart is deleted");

        else
            return new ResponseEntity<>(HttpStatusCode.valueOf(409));
    }

    // this is responsible for adding to wishlist
    @GetMapping("/addWish")
    public ResponseEntity<String> addWish(@RequestParam Integer pid, @RequestParam Long uid) {

        Wish wish = wishService.saveWish(pid, uid);
        if (!ObjectUtils.isEmpty(wish)) {
            return ResponseEntity.ok("Product already added in wishlist");
        } else {

            return ResponseEntity.ok("Product is added in wishlist");
        }

    }

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
     */
    @GetMapping("/wish")
    public ResponseEntity<List<Wish>> loadWishPage(@RequestParam Long userid) {

        List<Wish> wishlist = wishService.getAllWishByIdUserId(userid);

        return ResponseEntity.ok(wishlist);

    }

    @DeleteMapping("/api/wishlist")
    public ResponseEntity<String> DeleteWishList(@RequestParam Integer productid) {
        Boolean WishDelete = wishService.deleteWishProduct(productid);
        if (WishDelete)
            return ResponseEntity.ok("successfully wishlist is deleted");

        else
            return new ResponseEntity<>(HttpStatusCode.valueOf(409));
    }

    /*
     * private UserDtls getLoggedInUserDetails(Principal p) {
     * String email = p.getName();
     * UserDtls userDtls = userService.getUserByEmail(email);
     * return userDtls;
     * }
     */

    // this is responsible for increasing and decreasing amount of quantity in
    // cart......
    @GetMapping("/cartQuantityUpdate")
    public void cartQuantityUpdate(@RequestParam String sy, @RequestParam Integer cid) {
        cartService.updateQuantity(sy, cid);

    }

    /*
     * @GetMapping("/orders")
     * public String orderPage() {
     * 
     * List<Cart> carts = cartService.getCartByUsers(user.getId());
     * 
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

    @PostMapping("feedback/add")
    public ResponseEntity<String> saveFeed(@RequestBody FeedbackDto feedbackDto) {
        FeedbackDto feedback = feedbackService.saveFeedBack(feedbackDto);

        if (feedback != null) {
            return ResponseEntity.ok("Feedback is successfully saved");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Feedback could not be saved");
        }

    }

    @GetMapping("feedback/product/{productId}")
    public ResponseEntity<List<FeedbackDto>> getFeedbackForProduct(@PathVariable Integer ProductId) {

        List<FeedbackDto> feedback = feedbackService.getFeedbackByProductId(ProductId);

        return ResponseEntity.ok(feedback);
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