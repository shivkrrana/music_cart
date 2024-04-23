package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.UserDto;
import com.example.ecommerce.entity.User;

public interface UserMapper {

    static User toUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setMobileNo(userDto.getMobileNo());
        user.setPassword(userDto.getPassword());
        return user;
    }

}
