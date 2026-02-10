package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.constants.KycStatus;
import com.example.demo.constants.Role;
import com.example.demo.constants.errorTypes.AuthErrorType;
import com.example.demo.constants.errorTypes.UserErrorType;
import com.example.demo.exception.Auth.AuthException;
import com.example.demo.exception.User.UserException;
import com.example.demo.model.Artisan;
import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.repository.ArtisanRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.Artisan.ArtisanResponseDTO;
import com.example.demo.response.Others.DailyMetrics;
import com.example.demo.response.Order.OrderResponseDTO;
import com.example.demo.response.User.UserResponseDTO;
import com.example.demo.service.methods.AdminService;
import com.example.demo.service.methods.OrderService;
import com.example.demo.service.methods.UserService;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserService userService;

    @Autowired
    private ArtisanRepository artisanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Override
    public String approveArtisan(Long artisanId, String jwt) {

        User user = userService.getUserByJwt(jwt);
        if (user == null) {
            throw new AuthException("jwt token is invalid !!", AuthErrorType.TOKEN_INVALID);
        }
        if (!user.getRole().equals(Role.ADMIN)) {
            throw new UserException("only admin can approve artisans", UserErrorType.UNAUTHORIZED);
        }

        Artisan artisan = artisanRepository.findById(artisanId)
                .orElseThrow(() -> new UserException("artisan is not found", UserErrorType.NOT_FOUND));

        artisan.setKycStatus(KycStatus.APPROVED);
        artisanRepository.save(artisan);

        return "Artisan Approved Successfully";

    }

    @Override
    public List<ArtisanResponseDTO> getAllArtisans(String jwt) {
        User user = userService.getUserByJwt(jwt);
        if (user == null) {

            throw new AuthException("invalid or missing authorization token", AuthErrorType.TOKEN_INVALID);
        }

        if (user.getRole() == null || !user.getRole().equals(Role.ADMIN)) {
            throw new UserException("Only admin can access artisan profiles", UserErrorType.UNAUTHORIZED);

        }

        List<Artisan> artisanDetails = artisanRepository.findAll();

        return artisanDetails.stream().map(artisan -> {
            ArtisanResponseDTO dto = new ArtisanResponseDTO();
            User artisanUser = artisan.getUser();

            dto.setName(artisanUser.getName());
            dto.setUsername(artisanUser.getUsername());
            dto.setBrandName(artisan.getBrandName());
            dto.setArtisianType(artisan.getArtisianType());
            dto.setBio(artisan.getBio());
            dto.setPincode(artisan.getPincode());
            dto.setCity(artisan.getCity());
            dto.setState(artisan.getState());
            dto.setKycStatus(artisan.getKycStatus());

            return dto;
        }).toList();
    }

    @Override
    public String rejectArtisan(Long artisanId, String jwt) {

        User user = userService.getUserByJwt(jwt);
        if (user == null) {
            throw new AuthException("invalid or missing authorization token", AuthErrorType.TOKEN_INVALID);
        }

        if (!user.getRole().equals(Role.ADMIN)) {
            throw new UserException("Only admin can reject artisans", UserErrorType.UNAUTHORIZED);

        }

        Artisan artisan = artisanRepository.findById(artisanId)
                .orElseThrow(() -> new UserException("artisan is not found with id:", UserErrorType.NOT_FOUND));

        artisan.setKycStatus(KycStatus.REJECTED);
        artisanRepository.save(artisan);

        return "Artisan Rejected Successfully";
    }

    @Override
    public List<UserResponseDTO> getAllUsers(String jwt) {

        User user = userService.getUserByJwt(jwt);

        if (user == null) {
            throw new AuthException("invalid or missing authorization token", AuthErrorType.TOKEN_INVALID);
        }

        if (!user.getRole().equals(Role.ADMIN)) {
            throw new UserException("Only admin can access all users", UserErrorType.UNAUTHORIZED);
        }

        List<User> users = userService.getUsers();

        return users.stream().map(u -> {
            UserResponseDTO dto = new UserResponseDTO();
            dto.setId(u.getUserId());
            dto.setName(u.getName());
            dto.setUsername(u.getUsername());
            dto.setRole(u.getRole());
            return dto;
        }).toList();

    }

    @Override
    public String blockUser(Long userId, String jwt) {
        User adminUser = userService.getUserByJwt(jwt);
        if (adminUser == null) {
            throw new AuthException("invalid or missing authorization token", AuthErrorType.TOKEN_INVALID);
        }
        if (!adminUser.getRole().equals(Role.ADMIN)) {
            throw new UserException("Only admin can block users", UserErrorType.UNAUTHORIZED);
        }
        User userToBlock = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found with id: " + userId, UserErrorType.NOT_FOUND));
        userToBlock.setIsBlocked(true);
        userRepository.save(userToBlock);
        return "User blocked successfully";
    }

    @Override
    public List<OrderResponseDTO> getAllOrders(String jwt) {
        User user = userService.getUserByJwt(jwt);
        if (user == null) {
            throw new AuthException("invalid or missing authorization token", AuthErrorType.TOKEN_INVALID);
        }
        if (!user.getRole().equals(Role.ADMIN)) {
            throw new UserException("Only admin can access all orders", UserErrorType.UNAUTHORIZED);
        }
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(order -> {
            OrderResponseDTO dto = new OrderResponseDTO();
            // dto.setOrderId(order.getOrderId());
            dto.setUserId(order.getUser().getUserId());
            // dto.setArtisanId(order.getArtisan().getArtisanId());
            // dto.setOrderStatus(OrderStatus.valueOf(order.getOrderStatus()));
            dto.setTotalAmount(order.getTotalAmount());
            // dto.setCreatedAt(order.getCreatedAt());
            return dto;
        }).toList();
    }

    @Override
    public DailyMetrics getDailyMetrics(String jwt) {
        User user = userService.getUserByJwt(jwt);
        if (user == null) {
            throw new AuthException("invalid or missing authorization token", AuthErrorType.TOKEN_INVALID);
        }
        if (!user.getRole().equals(Role.ADMIN)) {
            throw new UserException("Only admin can access daily metrics", UserErrorType.UNAUTHORIZED);
        }
        DailyMetrics dailyMetrics = new DailyMetrics();
        dailyMetrics.setTotalUsers(userRepository.count());
        dailyMetrics.setTotalArtisans(artisanRepository.count());
        dailyMetrics.setTotalOrders(orderRepository.count());
        dailyMetrics.setTotalRevenue(orderService.getOrderCount().doubleValue());
        return dailyMetrics;
    }

}
