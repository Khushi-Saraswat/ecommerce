package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.constants.errorTypes.UserErrorType;
import com.example.demo.exception.User.UserException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    public UserInfoService() {

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userInfo = userRepo.findByusername(username);

        if (userInfo == null) {

            throw new UserException("user not found", UserErrorType.NOT_FOUND);

        }

        System.out.println("Loaded user: " + userInfo.getUsername());
        System.out.println("Authorities in userInfoServices: " + userInfo.getAuthorities());

        return userInfo;

    }

}
