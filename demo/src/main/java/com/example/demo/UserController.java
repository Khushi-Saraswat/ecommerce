package com.example.demo;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Cart;
import com.example.demo.model.Category;
import com.example.demo.model.Feedback;
import com.example.demo.model.OrderRequest;
//import com.example.demo.model.Product;
import com.example.demo.model.ProductOrder;
import com.example.demo.model.UserDtls;
import com.example.demo.model.Wish;
import com.example.demo.service.CartService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.FeedbackService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import com.example.demo.service.WishService;
import com.example.demo.util.CommonUtil;
import com.example.demo.util.OrderStatus;

//import java.util.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

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
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home() {
        return "user/home";
    }

    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {

        if (p != null) {
            String email = p.getName();
            UserDtls userDtls = userService.getUserByEmail(email);
            m.addAttribute("user", userDtls);
            Integer countCart = cartService.getCounterCart(userDtls.getId());
            m.addAttribute("countCart", countCart);

        }
        List<Category> allActiveCategory = categoryService.getAllActiveCategory();
        m.addAttribute("category", allActiveCategory);

    }

    @GetMapping("/addCart")
    public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session) {
        System.out.println(pid + "" + uid + "in addToCart");
        Cart saveCart = cartService.saveCart(pid, uid);

        if (ObjectUtils.isEmpty(saveCart)) {
            session.setAttribute("errorMsg", "Product add to cart failed");
        } else {
            session.setAttribute("succMsg", "Product added to cart");
        }
        return "redirect:/products/" + pid;
    }

    @GetMapping("/addWish")
    public String addWish(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session) {
        System.out.println(pid + "" + uid);
        Wish wish = wishService.saveWish(pid, uid);
        if (ObjectUtils.isEmpty(wish)) {
            session.setAttribute("errorMsg", "Product already added in wishlist");
        } else {
            session.setAttribute("succMsg", "Product added to wishlist");
        }
        return "redirect:/products/" + pid;
    }

    @GetMapping("/cart")
    public String loadCartPage(Principal p, Model m) {
        UserDtls user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartByUsers(user.getId());
        m.addAttribute("cart", carts);
        if (carts.size() > 0) {

            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            m.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        return "/user/cart";
    }

    @GetMapping("/wish")
    public String loadWishPage(Principal p, Model m) {
        UserDtls user = getLoggedInUserDetails(p);
        List<Wish> Wish = wishService.getAllWishByIdUserId(user.getId());
        System.out.println(Wish);
        m.addAttribute("wish", Wish);

        return "/user/Wishlist";
    }

    private UserDtls getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        UserDtls userDtls = userService.getUserByEmail(email);
        return userDtls;
    }

    @GetMapping("/cartQuantityUpdate")
    public String cartQuantityUpdate(@RequestParam String sy, @RequestParam Integer cid) {
        cartService.updateQuantity(sy, cid);
        return "redirect:/user/cart";
    }

    @GetMapping("/orders")
    public String orderPage(Principal p, Model m) {
        UserDtls user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartByUsers(user.getId());
        m.addAttribute("cart", carts);
        if (carts.size() > 0) {

            Double orderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice() + 250 + 100;
            m.addAttribute("totalOrderPrice", totalOrderPrice);
            m.addAttribute("orderPrice", orderPrice);
        }
        return "/user/orders";
    }

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute OrderRequest orderRequest, Principal p) throws Exception {
        System.out.println(orderRequest + "jsdg");

        UserDtls userdtls = getLoggedInUserDetails(p);
        orderService.saveOrder(userdtls.getId(), orderRequest);

        return "/user/success";
    }

    @GetMapping("/user-orders")
    public String myOrder(Model m, Principal p) {
        UserDtls userdtls = getLoggedInUserDetails(p);
        List<ProductOrder> orders = orderService.getOrdersByUser(userdtls.getId());
        m.addAttribute("orders", orders);
        return "/user/MyOrder";
    }

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

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword, @RequestParam String currentPassword, Principal p,
            HttpSession session) {
        UserDtls loggedInUserDetails = getLoggedInUserDetails(p);

        boolean matches = passwordEncoder.matches(currentPassword, loggedInUserDetails.getPassword());

        if (matches) {
            String encodePassword = passwordEncoder.encode(newPassword);
            loggedInUserDetails.setPassword(encodePassword);
            UserDtls updateUser = userService.updateUser(loggedInUserDetails);
            if (ObjectUtils.isEmpty(updateUser)) {
                session.setAttribute("errorMsg", "Password not updated !! Error in server");
            } else {
                session.setAttribute("succMsg", "Password Updated sucessfully");
            }
        } else {
            session.setAttribute("errorMsg", "Current Password incorrect");
        }

        return "redirect:/user/profile";
    }

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
