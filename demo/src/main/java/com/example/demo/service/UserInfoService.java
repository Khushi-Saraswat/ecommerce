package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.UserDtls;
import com.example.demo.repository.UserRepository;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    public UserInfoService() {

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDtls userInfo = userRepo.findByusername(username);

        if (userInfo == null) {

            throw new UserNotFoundException("User not found");

        }

        System.out.println("Loaded user: " + userInfo.getUsername());
        System.out.println("Authorities: " + userInfo.getAuthorities());

        return userInfo;

    }

}
