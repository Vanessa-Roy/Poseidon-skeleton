package com.springbootskeleton;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.CreateUserDto;
import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @InjectMocks
    UserService userServiceTest;

    @Mock
    UserRepository userRepositoryMock;

    @Mock
    private static PasswordEncoder passwordEncoder;

    private CreateUserDto createUserDto = new CreateUserDto();

    private UserDto userDto = new UserDto();
    private User user = new User();

    @Test
    public void saveUserDoesNotExistShouldCallTheUserRepositorySaveMethodTest() {

        userServiceTest.saveUser(createUserDto);

        verify(userRepositoryMock, Mockito.times(1)).save(any(User.class));
    }

    @Test
    public void updateUserShouldCallTheUserRepositorySaveMethodTest() {

        user = new User(
                1,
                "fullname",
                "passwordTest0!",
                "username",
                "role"
        );

        createUserDto = new CreateUserDto(
                1,
                "fullname2",
                "passwordTest0!2",
                "username2",
                "role"
        );


        userServiceTest.updateUser(user, createUserDto);

        verify(userRepositoryMock, Mockito.times(1)).save(any(User.class));
    }

    @Test
    public void deleteUserShouldCallTheUserRepositoryDeleteMethodTest() {

        userServiceTest.deleteUser(user);

        verify(userRepositoryMock, Mockito.times(1)).delete(user);
    }

    @Test
    public void loadUserDtoListShouldReturnAllTheUsersDtoTest() {

        List<User> userList = new ArrayList<>(List.of(
                new User(1, "fullname", "passwordTest0!", "username", "role")
        ));

        when(userRepositoryMock.findAll()).thenReturn(userList);

        List<UserDto> userDtoList = userServiceTest.loadUserDtoList();

        verify(userRepositoryMock, Mockito.times(1)).findAll();
        assertEquals(userList.get(0).getId(), userDtoList.get(0).getId());
        assertEquals(userList.get(0).getFullname(), userDtoList.get(0).getFullname());
        assertEquals(userList.get(0).getUsername(), userDtoList.get(0).getUsername());
        assertEquals(userList.get(0).getRole(), userDtoList.get(0).getRole());
    }

    @Test
    public void loadUserDtoByIdShouldReturnAnUserDtoTest() {

        User user = new User(1, "fullname", "passwordTest0!", "username", "role");

        when(userRepositoryMock.findById(user.getId())).thenReturn(Optional.of(user));

        CreateUserDto userDto = userServiceTest.loadUserDtoById(user.getId());

        verify(userRepositoryMock, Mockito.times(1)).findById(user.getId());
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getFullname(), userDto.getFullname());
        assertEquals(user.getPassword(), userDto.getPassword());
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getRole(), userDto.getRole());
    }

    @Test
    public void loadUserDtoByIdWithUnknownIdShouldThrowAnExceptionTest() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userServiceTest.loadUserDtoById(2));
        assertEquals("Invalid User Id:" + 2, exception.getMessage());
    }

    @Test
    public void mapToUserDTOShouldReturnAUserDtoFromAUserEntityTest() {

        User user = new User(1, "fullname", "passwordTest0!", "username", "role");

        UserDto userDto = userServiceTest.mapToUserDTO(user);

        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getFullname(), userDto.getFullname());
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getRole(), userDto.getRole());
    }
}
