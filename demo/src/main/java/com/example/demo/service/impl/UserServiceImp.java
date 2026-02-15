package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.example.demo.Common.AbstractMapperService;
import com.example.demo.config.JwtService;
import com.example.demo.constants.errorTypes.OrderErrorType;
import com.example.demo.dto.OrderDto;
import com.example.demo.exception.Order.OrderException;
import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.User.UserRequestDTO;
import com.example.demo.response.Order.OrderStatusResponse;
import com.example.demo.response.User.UserResponseDTO;
import com.example.demo.service.methods.UserService;

@Service
public class UserServiceImp implements UserService {

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private AbstractMapperService abstractMapperService;

   @Autowired
   private JwtService jwtService;

   @Autowired
   private OrderRepository orderRepository;

   @Autowired
   private ProductRepository productRepository;

   @Override
   public User getUserByEmail(String email) {
      return userRepository.findByusername(email);
   }

   /*
    * @Override
    * public List<User> getUsers(String role) {
    * return userRepository.findByrole(role);
    * }
    */

   /*
    * @Override
    * public Boolean updateAccountStatus(Integer id, Boolean status) {
    * Optional<UserDtls> findByUser = userRepository.findById(id);
    * if (findByUser.isPresent()) {
    * UserDtls userDtls = findByUser.get();
    * userDtls.setIsEnable(status);
    * userRepository.save(userDtls);
    * return true;
    * }
    * return false;
    * }
    */

   // @Override
   // public void increaseFailedAttempt(User user) {
   // int attempt = user.getFailedAttempt() + 1;
   // user.setFailedAttempt(attempt);
   // userRepository.save(user);
   // }

   // @Override
   // public void userAccountLock(User user) {
   // user.setAccountNonLocked(false);
   // user.setLockTime(new Date());
   // userRepository.save(user);

   // }

   /*
    * @Override
    * public boolean unlockAccountTimeExpired(User user) {
    * // locked time
    * long lockTime = user.getLockTime().getTime();
    * // unlock time
    * long unlockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;
    * // current time..
    * long currentTime = System.currentTimeMillis();
    * // when unlock time is less than currentTime
    * if (unlockTime < currentTime) {
    * user.setAccountNonLocked(true);
    * user.setFailedAttempt(0);
    * user.setLockTime(null);
    * userRepository.save(user);
    * return true;
    * }
    * return false;
    * 
    * }
    */

   @Override
   public void resetAttempt(int userId) {

   }

   @Override
   public void updateUserResetToken(String email, String resetToken) {
      User findByemail = userRepository.findByusername(email);
      findByemail.setResetToken(resetToken);
      userRepository.save(findByemail);

   }

   @Override
   public User getUserByToken(String token) {
      return userRepository.findByResetToken(token);
   }

   @Override
   public User updateUser(User user) {
      return userRepository.save(user);
   }

   @Override
   public UserResponseDTO updateUserProfile(Long id, UserRequestDTO userDtlsDto) {
      User saveUser = null;
      User user = (User) abstractMapperService
            .toEntity(userDtlsDto, User.class);
      User dbuser = userRepository.findById(id).get();

      // allow only username and mobile number to update
      if (!ObjectUtils.isEmpty(dbuser)) {
         dbuser.setName(user.getName());
         dbuser.setMobileNumber(user.getMobileNumber());
         saveUser = userRepository.save(dbuser);

      }

      return abstractMapperService
            .toDto(saveUser, UserResponseDTO.class);
   }

   @Override
   public UserResponseDTO UserByToken(String token) {
      // Remove "Bearer " prefix if present
      if (token != null && token.startsWith("Bearer ")) {
         token = token.substring(7);
      }
      System.out.println(token + "token given");
      String email = jwtService.extractUsername(token);
      User userDtls = userRepository.findByusername(email);
      return abstractMapperService
            .toDto(userDtls, UserResponseDTO.class);
   }

   @Override
   public List<OrderDto> getOrdersByUserId(Long userId) {
      List<OrderDto> orders = userRepository.findOrdersByUserId(userId);
      return orders;
   }

   @Override
   public User getUserByJwt(String jwt) {

      if (jwt != null && jwt.startsWith("Bearer ")) {
         jwt = jwt.substring(7);
      }
      System.out.println(jwt + "token given");
      String email = jwtService.extractUsername(jwt);
      User userDtls = userRepository.findByusername(email);
      return userDtls;
   }

   @Override
   public UserResponseDTO getProfile(String jwt) {

      return UserByToken(jwt);

   }

   @Override
   public List<User> getUsers() {
      return userRepository.findAll();
   }

   @Override
   public OrderStatusResponse trackOrder(String orderId) {
      Optional<Order> order = orderRepository.findById(Long.valueOf(orderId));

      if (order.isPresent()) {
         OrderStatusResponse response = new OrderStatusResponse();
         response.setStatus(order.get().getOrderStatus());
         return response;
      }

      throw new OrderException("Order not found", OrderErrorType.ORDER_NOT_FOUND);
   }

   @Override
   public UserResponseDTO Profile() {
      // TODO Auto-generated method stub
      return null;
   }

}
