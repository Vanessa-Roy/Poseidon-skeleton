package com.nnk.springboot.dto;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String fullname;
    private String username;
    private Role role;

    public static UserDto mapFromUser(User user) {
        return new UserDto(
                user.getId(),user.getFullname(),user.getUsername(),user.getRole());
    }

    public static List<UserDto> mapFromUsers(List<User> users) {
        return users.stream().map(UserDto::mapFromUser).toList();
    }
}
