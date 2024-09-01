package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.ProductOrder;
import com.example.demo.model.UserDtls;
import com.example.demo.repository.Feedbackrepo;
import com.example.demo.service.CartService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.FeedbackService;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import com.example.demo.util.CommonUtil;
import com.example.demo.util.OrderStatus;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private Feedbackrepo feedbackrepo;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderservice;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index() {
        return "admin/index";
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

    @GetMapping("/loadAddProduct")
    public String loadAddProduct(Model m) {
        List<Category> categories = categoryService.getAllCategory();
        m.addAttribute("categories", categories);
        return "admin/addProduct";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id, HttpSession session) {
        System.out.println(id + "in deleting product from admin side");
        Boolean deleteProduct = productService.deleteProduct(id);
        if (deleteProduct) {
            session.setAttribute("succMsg", "Product delete success");
        } else {
            session.setAttribute("errorMsg", "Something wrong on server");
        }
        return "redirect:/admin/product";

    }

    @GetMapping("/Category")
    public String loadCategory(Model m, @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize) {

        Page<Category> page = categoryService.getAllActiveCategoryPagination(pageNo, pageSize);
        List<Category> category = page.getContent();
        m.addAttribute("categorys", category);
        m.addAttribute("CategorySize", category.size());
        System.out.println(category.size() + "category size");
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

        // m.addAttribute("categorys", categoryService.getAllCategory());
        return "admin/Category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute("Category") Category category,
            @RequestParam("img") MultipartFile file,
            HttpSession session) throws IOException {
        System.out.println(category.getIsActive());
        System.out.println(category.getName());
        // System.out.println(category.getImageName());
        if (file == null) {
            System.out.println("file is empty");
        } else {
            System.out.println("file is not empty");
        }
        String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
        category.setImageName(imageName);

        Boolean existCategory = categoryService.existCategory(category.getName());

        if (existCategory) {
            session.setAttribute("errorMsg", "Category Name already exists");
        } else {

            Category saveCategory = categoryService.saveCategory(category);

            if (ObjectUtils.isEmpty(saveCategory)) {
                session.setAttribute("errorMsg", "Not saved ! internal server error");
            } else {

                try (InputStream inputStream = file.getInputStream()) {
                    Path path = Paths.get("C:\\Users\\DELL\\Desktop\\ecommerce\\demo\\src\\main\\resources\\static\\img"
                            + File.separator + "category_img" + File.separator
                            + file.getOriginalFilename());
                    System.out.println(path);
                    Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

            }
        }

        return "redirect:/admin/Category";
    }

    private UserDtls getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        UserDtls userDtls = userService.getUserByEmail(email);
        return userDtls;
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession httpSession) {

        Boolean deleteCategory = categoryService.deleteCategory(id);
        if (deleteCategory) {
            httpSession.setAttribute("succMsg", "Category delete success");
        } else {
            httpSession.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/admin/Category";
    }

    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable int id, Model m) {
        m.addAttribute("category", categoryService.getCategoryById(id));
        return "admin/edit_category";
    }

    @PostMapping("/updateCategory")
    public String UpdateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
            HttpSession httpSession) {
        Category oldcategory = categoryService.getCategoryById(category.getId());
        System.out.println(category.getId());
        String imageName = file.isEmpty() ? oldcategory.getImageName() : file.getOriginalFilename();
        if (!ObjectUtils.isEmpty(category)) {
            oldcategory.setName(category.getName());
            oldcategory.setIsActive(category.getIsActive());
            oldcategory.setImageName(imageName);
        }
        Category updateCategory = categoryService.saveCategory(oldcategory);
        if (!ObjectUtils.isEmpty(updateCategory)) {
            if (!file.isEmpty()) {

                try (InputStream inputStream = file.getInputStream()) {
                    Path path = Paths.get("C:\\Users\\DELL\\Desktop\\ecommerce\\demo\\src\\main\\resources\\static\\img"
                            + File.separator + "category_img" + File.separator
                            + file.getOriginalFilename());

                    System.out.println(path);
                    Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                httpSession.setAttribute("succMsg", "Category update success");
            }

        } else {

            httpSession.setAttribute("errorMsg", "something wrong on server");
        }
        return "redirect:/admin/loadEditCategory/" + category.getId();
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
            HttpSession session) throws IOException {

        String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();

        product.setImage(imageName);
        product.setDiscount(0);
        product.setDiscountPrice(product.getPrice());
        Product saveProduct = productService.saveProduct(product);

        if (!ObjectUtils.isEmpty(saveProduct)) {

            try (InputStream inputStream = image.getInputStream()) {
                Path path = Paths.get("C:\\Users\\DELL\\Desktop\\ecommerce\\demo\\src\\main\\resources\\static\\img"
                        + File.separator + "product_img" + File.separator
                        + image.getOriginalFilename());

                // System.out.println(path);
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            ;

            session.setAttribute("succMsg", "Product Saved Success");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/admin/loadAddProduct";
    }

    @GetMapping("/product")
    public String loadViewProduct(Model e, @RequestParam(defaultValue = "") String ch,
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize) {
        /*
         * List<Product> products = null;
         * if (ch != null && ch.length() > 0) {
         * products = productService.searchProduct(ch);
         * } else {
         * products = productService.getAllProducts();
         * }
         */

        Page<Product> page = null;
        if (ch != null && ch.length() > 0) {
            page = productService.searchProductPagination(pageNo, pageSize, ch);
        } else {
            page = productService.getAllProducts(pageNo, pageSize);
        }
        e.addAttribute("products", page.getContent());
        e.addAttribute("pageNo", page.getNumber());
        System.out.println(page.getNumber() + "pagenumber");
        e.addAttribute("pageSize", pageSize);
        System.out.println(pageSize + "pageSize");
        e.addAttribute("totalElements", page.getTotalElements());
        System.out.println("totalElements" + page.getTotalElements());
        e.addAttribute("totalPages", page.getTotalPages());
        System.out.println("totalPages" + page.getTotalElements());
        e.addAttribute("isFirst", page.isFirst());
        System.out.println("isFirst" + page.isFirst());
        e.addAttribute("isLast", page.isLast());
        System.out.println("isLast" + page.isLast());
        return "admin/product";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable int id, Model e) {
        e.addAttribute("product", productService.getProductById(id));
        e.addAttribute("category", categoryService.getAllCategory());
        return "admin/edit_product";
    }

    @PostMapping("/updateProduct")
    public String editProduct(@ModelAttribute Product pro, @RequestParam("file") MultipartFile image,
            HttpSession session) {
        // Product Product = productService.updateProduct(pro,image);
        if (pro.getDiscount() < 0 || pro.getDiscount() > 100) {
            session.setAttribute("errorMsg", "invalid Discount");
        } else {
            Product updateProduct = productService.updateProduct(pro, image);
            if (!ObjectUtils.isEmpty(updateProduct)) {
                session.setAttribute("succMsg", "Product update success");
            } else {
                session.setAttribute("errorMsg", "Something wrong on server");
            }
        }
        return "redirect:/admin/editProduct/" + pro.getId();

    }

    @GetMapping("/users")
    public String getAllUsers(Model m, @RequestParam Integer type) {
        List<UserDtls> users = null;
        if (type == 1) {
            users = userService.getUsers("ROLE_USER");
        } else {
            users = userService.getUsers("ROLE_ADMIN");
        }
        m.addAttribute("userType", type);
        m.addAttribute("users", users);
        return "/admin/users";
    }

    @GetMapping("/updateSts")
    public String updateUserAccountStatus(@RequestParam Boolean status, @RequestParam Integer id,
            @RequestParam String type, HttpSession session) {
        System.out.println(type + "type in update status..");
        Boolean f = userService.updateAccountStatus(id, status);
        if (f) {
            session.setAttribute("succMsg", "Account Status Updated");
        } else {
            session.setAttribute("errorMsg", "Something wrong on server");
        }
        return "redirect:/admin/users?type=" + type;
    }

    @GetMapping("/orders")
    public String getAllOrders(Model m,
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "2") Integer pageSize) {
        // List<ProductOrder> allOrders = orderservice.getAllOrders();
        // m.addAttribute("orders", allOrders);
        // m.addAttribute("search", false);
        Page<ProductOrder> page = orderservice.getAllActiveOrderPagination(pageNo, pageSize);
        m.addAttribute("search", false);
        List<ProductOrder> order = page.getContent();
        m.addAttribute("orders", order);
        m.addAttribute("OrderSize", order.size());
        System.out.println(order.size() + "order size");
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
        return "admin/orders";
    }

    @PostMapping("/update-order-status")
    public String updateOrderStatus(Model m, @RequestParam Integer id, @RequestParam Integer st, HttpSession session) {
        OrderStatus[] values = OrderStatus.values();
        String status = null;
        for (OrderStatus orderSt : values) {
            if (orderSt.getId().equals(st)) {
                System.out.println(orderSt.getId());
                status = orderSt.getName();
            }
        }
        ProductOrder updateOrder = orderservice.updatOrderStatus(id, status);
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

        System.out.println(values + "values");
        return "redirect:/admin/orders";
    }

    @GetMapping("/search-order")
    public String searchProduct(@RequestParam String OrderId, Model m, HttpSession session,
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "2") Integer pageSize) {
        if (OrderId != null) {
            ProductOrder order = orderservice.getOrdersByOrderId(OrderId.trim());

            if (ObjectUtils.isEmpty(order)) {
                session.setAttribute("errorMsg", "Incorrect orderId!!!");
                m.addAttribute("orderDtls", null);
            } else {
                m.addAttribute("orderDtls", order);

            }
            m.addAttribute("search", true);
        } else {
            // List<ProductOrder> allOrders = orderservice.getAllOrders();
            // m.addAttribute("orders", allOrders);
            // m.addAttribute("search", false);
            Page<ProductOrder> page = orderservice.getAllActiveOrderPagination(pageNo, pageSize);
            m.addAttribute("orders", page);
            m.addAttribute("search", false);
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
        }

        return "/admin/orders";
    }

    @GetMapping("/add-admin")
    public String loadadminAdd() {

        return "admin/add_admin";
    }

    @GetMapping("/feedback-dashboard")
    public String feedBackDash(Model m) {
        m.addAttribute("feedbackList", feedbackService.getAllFeedBack());
        m.addAttribute("totalFeedbacks", feedbackrepo.count());
        m.addAttribute("totalProductFeedbacks", feedbackrepo.countByCategory("Product feedback"));
        m.addAttribute("totalCustomerFeedbacks", feedbackrepo.countByCategory("Customer feedback"));
        m.addAttribute("totalReviewFeedbacks", feedbackrepo.countByCategory("Review feedback"));
        m.addAttribute("totalOtherFeedbacks", feedbackrepo.countByCategory("others"));

        return "admin/FeedbackDashboard";
    }

    @PostMapping("/save-admin")
    public String saveAdmin(@ModelAttribute UserDtls user, @RequestParam("img") MultipartFile file,
            HttpSession httpSession) {
        String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setProfileimage(imageName);
        UserDtls saveUser = userService.saveAdmin(user);
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
        return "admin/add_admin";

    }

    @GetMapping("/profile")
    public String profile() {
        return "/admin/profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute UserDtls user, @ModelAttribute MultipartFile img, HttpSession session) {
        UserDtls dbuser = userService.updateUserProfile(user, img);
        if (ObjectUtils.isEmpty(dbuser)) {
            session.setAttribute("errorMsg", "Profile not updated");
        } else {
            session.setAttribute("succMsg", "Profile Updated");
        }
        return "redirect:/admin/profile";
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

        return "redirect:/admin/profile";
    }

}
