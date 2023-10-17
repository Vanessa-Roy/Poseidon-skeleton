package com.nnk.springboot.dto;

import com.nnk.springboot.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SaveUserDto {
    private Integer id;
    @NotBlank(message = "Fullname is mandatory")
    private String fullname;
    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
            message = "password must contain 8 characters with at least one digit, one special character, one lowercase letter and one uppercase letter"
    )
    private String password;
    @NotBlank(message = "Username is mandatory")
    private String username;
    @NotNull(message = "Role is mandatory")
    private User.Role role;

    public static SaveUserDto mapToSaveUserDTO(User user) {
        return new SaveUserDto(user.getId(),user.getFullname(),user.getPassword(),user.getUsername(), user.getRole());
    }
}
