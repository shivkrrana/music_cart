package com.example.ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    @NotBlank
    private String name;
    @NotBlank
    @Pattern(regexp = "^[6-9]\\d{9}$")
    private String mobileNo;
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    private String email;
    @NotBlank(message = "You can pass password as empty")
    private String password;
}
