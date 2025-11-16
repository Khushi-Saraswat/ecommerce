package com.example.demo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.service.UserInfoService;

//marks the class as the spring configuration class.....
@Configuration
// enable spring security web features......
@EnableWebSecurity
public class SecurityConfig {

    /*
     * @Autowired
     * 
     * @Lazy
     * private AuthFailerHandlerImpl authenticationfailureHandler;
     * 
     * @Autowired
     * private JwtAuthFilter jwtAuthFilter;
     * 
     * DaoAuthenticationProvider daoAuthenticationProvider;
     * 
     * @Bean
     * public BCryptPasswordEncoder passwordEncoder() {
     * return new BCryptPasswordEncoder();
     * }
     * 
     * @Bean
     * public UserDetailsService getDetailsService() {
     * return new UserInfoService();
     * }
     * 
     * // check whether the email and mobile number is correct or not...
     */

    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    // used for encoding and decoding passwords.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // used for authenticating users. it delegate authentication request to various
    // authenticationProvider implementations.
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    // Interfaces responsible for authenticating users based on specific credentials
    // (e.g., DaoAuthenticationProvider for username/password,
    // JwtAuthenticationProvider for JWT tokens).
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userInfoService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;

    }

    // responsible for incoming requests,handling authentication,authorization and
    // other security features.
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

                // define which url's and request patterns require authentication.
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers("/api/auth/welcome", "/api/auth/GenerateToken", "/api/auth/register",
                                        "/forgot-password", "/reset-password", "/api/auth/refresh", "/api/auth/logout")
                                .permitAll()
                                .anyRequest().authenticated())
                // Stateless session-for jwt implementation
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authenticationProvider(authenticationProvider())

                // Add JWT filter before Spring Security's default filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        configuration.setAllowedMethods(List.of("GET", "POST"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
