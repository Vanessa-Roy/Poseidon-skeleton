package com.nnk.springboot.dto;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    @NotNull(message = "Role is mandatory")
    private Role role;

    public static UserDto mapFromUser(User user) {
        return new UserDto(
                user.getId(),user.getFullname(),user.getUsername(),user.getRole());
    }

    public static List<UserDto> mapFromUsers(List<User> users) {
        return users.stream().map(UserDto::mapFromUser).toList();
    }
}
