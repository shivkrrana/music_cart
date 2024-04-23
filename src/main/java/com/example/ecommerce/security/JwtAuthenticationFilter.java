package com.example.ecommerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {

        String requestHeader = request.getHeader("Authorization");

//        logger.info(" Header :  {}", requestHeader);

        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            //looking good
            String token = requestHeader.substring(7);
            try {
                boolean validateToken = jwtHelper.verifyToken(token);
                if (validateToken) {
                    //set the authentication
                    String userEmail = jwtHelper.getEmailFromToken(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    logger.info("Validation fails !!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            logger.info("Invalid Header Value !! ");
        }

        filterChain.doFilter(request, response);
    }
}
