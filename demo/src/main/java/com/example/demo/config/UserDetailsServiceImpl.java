package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserDtls;
import com.example.demo.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    
    @Autowired
    private UserRepository UserRepository;

    
    @Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		UserDtls userDtls = UserRepository.findByEmail(email);

		if (userDtls == null) {
			throw new UsernameNotFoundException("user not found");
		} else {
			return new CustomUser(userDtls);
		}
    
	}
}
