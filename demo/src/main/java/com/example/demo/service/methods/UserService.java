package com.example.demo.service.methods;

import java.util.List;

import com.example.demo.dto.OrderDto;
import com.example.demo.model.User;
import com.example.demo.request.User.UserRequestDTO;
import com.example.demo.response.Order.OrderStatusResponse;
import com.example.demo.response.User.UserResponseDTO;

public interface UserService {

    public User getUserByEmail(String email);

    public List<User> getUsers();

    // public Boolean updateAccountStatus(Integer id, Boolean status);

    public List<OrderDto> getOrdersByUserId(Long userId);

    // public void increaseFailedAttempt(User user);

    // public void userAccountLock(User user);

    // public boolean unlockAccountTimeExpired(User user);

    public void resetAttempt(int userId);

    public void updateUserResetToken(String email, String resetToken);

    public User getUserByToken(String resetToken);

    public User getUserByJwt(String jwt);

    public User updateUser(User user);

    public UserResponseDTO updateUserProfile(Long id, UserRequestDTO user);

    public UserResponseDTO UserByToken(String token);

    public UserResponseDTO getProfile(String jwt);

    public UserResponseDTO Profile();

    public OrderStatusResponse trackOrder(String orderId);

    // public List<> getProductsByCategory(Long categoryId);

}
