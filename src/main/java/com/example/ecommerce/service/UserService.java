package com.example.ecommerce.service;

import com.example.ecommerce.dto.UserDto;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.exception.CustomException;
import com.example.ecommerce.mapper.UserMapper;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.security.JwtHelper;
import com.example.ecommerce.security.ResponseToken;
import com.example.ecommerce.security.UserPrincipal;
import com.example.ecommerce.validator.PatternValidator;
import com.nimbusds.jose.KeyLengthException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    JwtHelper jwtHelper;

    @Autowired
    PatternValidator patternValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseToken registerUser(UserDto userDto) throws KeyLengthException {
        User existingEmail = userRepository.findByEmail(userDto.getEmail());
        if (existingEmail != null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "User already exists by given email.");
        }
        if (userRepository.existsByMobileNo(userDto.getMobileNo())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Given mobile number already exist.");
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = UserMapper.toUser(userDto);
        userRepository.save(user);

        String token = jwtHelper.generateToken(userDto.getEmail());
        return new ResponseToken(token);
    }

    public ResponseToken loginUser(UserDto userDto) throws KeyLengthException {
            User existingUser = userRepository.findByEmail(userDto.getEmail());

            if (existingUser == null) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Invalid Credentials");
            }
            if(!passwordEncoder.matches(userDto.getPassword(), existingUser.getPassword())){
                throw new CustomException(HttpStatus.BAD_REQUEST, "Invalid Credentials");
            }
            String token = jwtHelper.generateToken(userDto.getEmail());
            return new ResponseToken(token);
    }

    public User getUser(Integer userId) {
        return  userRepository.findById(userId).orElse(null);
    }

    public UserPrincipal getUserPrincipal(){
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return (UserPrincipal) authenticationToken.getPrincipal();
    }

    public String getUserName(){
        UserPrincipal userPrincipal = getUserPrincipal();
        User user = userRepository.findByEmail(userPrincipal.getUsername());
        return user.getName();
    }
}
