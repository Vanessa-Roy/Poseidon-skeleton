package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    @NotBlank(message = "Full Name is mandatory")
    private String fullname;
    @NotBlank(message = "User Name is mandatory")
    private String username;
    @NotBlank(message = "Role is mandatory")
    private String role;
}
