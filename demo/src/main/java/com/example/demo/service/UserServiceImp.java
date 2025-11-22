package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.UserDtls;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.methods.UserService;
import com.example.demo.util.AppConstant;

@Service
public class UserServiceImp implements UserService {

   private final PasswordEncoder passwordEncoder;

   @Autowired
   private UserRepository userRepository;

   UserServiceImp(PasswordEncoder passwordEncoder) {
      this.passwordEncoder = passwordEncoder;
   }

   @Override
   public UserDtls saveUser(UserDtls user) {

      user.setIsEnable(true);
      user.setAccountNonLocked(true);
      user.setFailedAttempt(0);
      user.setLockTime(null);
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      // String encodePassword = passwordEncoder.encode(user.getPassword());
      // user.setPassword(user.getPassword());
      // user.setUsername(user.getUsername());
      // user.setName(user.getName());
      // user.setMobileNumber(user.getMobileNumber());
      UserDtls saveUser = userRepository.save(user);
      return saveUser;
   }

   @Override
   public UserDtls getUserByEmail(String email) {
      return userRepository.findByusername(email);
   }

   @Override
   public List<UserDtls> getUsers(String role) {
      return userRepository.findByroles(role);
   }

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

   @Override
   public void increaseFailedAttempt(UserDtls user) {
      int attempt = user.getFailedAttempt() + 1;
      user.setFailedAttempt(attempt);
      userRepository.save(user);
   }

   @Override
   public void userAccountLock(UserDtls user) {
      user.setAccountNonLocked(false);
      user.setLockTime(new Date());
      userRepository.save(user);

   }

   @Override
   public boolean unlockAccountTimeExpired(UserDtls user) {
      // locked time
      long lockTime = user.getLockTime().getTime();
      // unlock time
      long unlockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;
      // current time..
      long currentTime = System.currentTimeMillis();
      // when unlock time is less than currentTime
      if (unlockTime < currentTime) {
         user.setAccountNonLocked(true);
         user.setFailedAttempt(0);
         user.setLockTime(null);
         userRepository.save(user);
         return true;
      }
      return false;

   }

   @Override
   public void resetAttempt(int userId) {

   }

   @Override
   public void updateUserResetToken(String email, String resetToken) {
      UserDtls findByemail = userRepository.findByusername(email);
      findByemail.setResetToken(resetToken);
      userRepository.save(findByemail);

   }

   @Override
   public UserDtls getUserByToken(String token) {
      return userRepository.findByResetToken(token);
   }

   @Override
   public UserDtls updateUser(UserDtls user) {
      return userRepository.save(user);
   }

   @Override
   public UserDtls updateUserProfile(UserDtls user, @RequestParam("file") MultipartFile file) {
      UserDtls saveUser = null;
      UserDtls dbuser = userRepository.findById(user.getId()).get();
      String image = file.isEmpty() ? dbuser.getProfileimage() : file.getOriginalFilename();
      System.out.println("image is: " + image);
      if (!file.isEmpty()) {
         dbuser.setProfileimage(image);
      } else {
         System.out.println("null");
      }
      if (!ObjectUtils.isEmpty(dbuser)) {
         dbuser.setName(user.getName());
         dbuser.setMobileNumber(user.getMobileNumber());
         saveUser = userRepository.save(dbuser);

      }
      try {

         if (!file.isEmpty()) {
            try (InputStream inputStream = file.getInputStream()) {
               Path path = Paths.get("C:\\Users\\DELL\\Desktop\\ecommerce\\demo\\src\\main\\resources\\static\\img"
                     + File.separator + "profile_img" + File.separator
                     + file.getOriginalFilename());

               System.out.println(path + "path is");
               Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ioe) {
               ioe.printStackTrace();
            }

         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return saveUser;
   }

   @Override
   public UserDtls saveAdmin(UserDtls user) {
      user.setRoles("ROLE_ADMIN");
      user.setIsEnable(true);
      user.setAccountNonLocked(true);
      user.setFailedAttempt(0);
      user.setLockTime(null);
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      // String encodePassword = passwordEncoder.encode(user.getPassword());
      // user.setPassword(user.getPassword());
      // user.setPassword(user.getPassword());
      // user.setUsername(user.getUsername());
      // user.setName(user.getName());
      // user.setMobileNumber(user.getMobileNumber());
      UserDtls saveUser = userRepository.save(user);
      return saveUser;
   }
}
