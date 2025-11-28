package com.example.demo.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.impl.UserInfoService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    public UserInfoService userInfoService;

    @Autowired
    private JwtService jwtService;

    // This is the main entry point of the filter for every
    // incoming HTTP request. it calls filterChain.doFilter(request, response)
    // at the end to pass the request along the filter chain to its final
    // destination (e.g., a controller), either after successfully setting the
    // user's security context or after failing authentication but allowing access
    // to public endpoints.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        System.out.println("Servlet path in filter: " + path);
        if (request.getServletPath().equals("/api/auth/GenerateToken")
                || request.getServletPath().equals("/api/auth/register")
                || request.getServletPath().equals("/api/auth/refresh")
                || request.getServletPath().equals("/api/auth/logout")
                || request.getServletPath().equals("/api/auth/forgot-password")
                || request.getServletPath().equals("/api/auth/reset-password")

        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }

        // SecurityContextHolder.getContext() checks if a user is already authenticated.
        // If an Authentication object is already present,
        // the filter skips JWT processing to avoid doing the same work again.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // retrieves the full, up-to-date user details from the database or user
            // service.
            UserDetails userDetails = userInfoService.loadUserByUsername(username);

            // setAuthenticationContext() takes the UserDetails, creates an Authentication
            // object (like UsernamePasswordAuthenticationToken), and stores it in
            // SecurityContextHolder, marking the user as authenticated for
            // that request so controllers and security checks can recognize them.
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}
