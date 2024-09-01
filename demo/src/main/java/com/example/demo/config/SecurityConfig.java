package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    @Lazy
    private AuthFailerHandlerImpl authenticationfailureHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService getDetailsService() {
        return new UserDetailsServiceImpl();
    }

    // check whether the email and mobile number is correct or not...
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(getDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        /*
         * .csrf(csrf -> csrf.disable())
         * .authorizeHttpRequests(authorizeRequests -> authorizeRequests
         * .requestMatchers("/user/**").hasRole("USER")// Allow access to / without
         * authentication
         * .requestMatchers("/admin/**").hasRole("ADMIN")
         * .requestMatchers("/**").permitAll())
         * 
         * .formLogin(formLogin -> formLogin
         * .loginPage("/signin") // Customize your login page
         * .loginProcessingUrl("/login") // URL to process login
         * .defaultSuccessUrl("/") // Redirect URL after successful login
         * .failureHandler(authenticationfailureHandler)
         * .successHandler(authenticationSuccessHandler)) // Allow everyone to access
         * the login page
         * .logout(logout -> logout
         * // .logoutUrl("/userlogout") // URL to trigger logout
         * // .logoutSuccessUrl("/userlogout") // Redirect URL after successful logout
         * .permitAll()); // Allow everyone to access the logout URL
         */

        http.csrf(csrf -> csrf.disable()).cors(cors -> cors.disable())
                .authorizeHttpRequests(req -> req.requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/**").permitAll())
                .formLogin(form -> form.loginPage("/signin")
                        .loginProcessingUrl("/login")
                        // .defaultSuccessUrl("/")
                        .failureHandler(authenticationfailureHandler)
                        .successHandler(authenticationSuccessHandler))
                .logout(logout -> logout.permitAll());

        return http.build();

    }

}
