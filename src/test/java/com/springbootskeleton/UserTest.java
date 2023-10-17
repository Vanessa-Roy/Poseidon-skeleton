package com.springbootskeleton;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @InjectMocks
    UserService userServiceTest;

    @Mock
    UserRepository userRepositoryMock;

    @Mock
    BCryptPasswordEncoder encoderMock;

    @Test
    public void saveUserDoesNotExistShouldCallTheUserRepositorySaveMethodTest() {

        User user = new User(1,"fullnameTest","Password0!","usernameTest", User.Role.USER);
        when(encoderMock.encode(user.getPassword())).thenReturn(user.getPassword());

        userServiceTest.createUser(user);

        verify(encoderMock, Mockito.times(1)).encode(user.getPassword());
        verify(userRepositoryMock, Mockito.times(1)).save(user);
    }

    @Test
    public void updateUserShouldCallTheUserRepositorySaveMethodTest() {

        User user = new User(1,"fullnameTest","Password0!","usernameTest", User.Role.USER);
        when(encoderMock.encode(user.getPassword())).thenReturn(user.getPassword());

        userServiceTest.updateUser(user);

        verify(encoderMock, Mockito.times(1)).encode(anyString());
        verify(userRepositoryMock, Mockito.times(1)).save(any(User.class));
    }

    @Test
    public void deleteUserShouldCallTheUserRepositoryDeleteMethodTest() {

        User user = new User(1,"fullnameTest","Password0!","usernameTest", User.Role.USER);

        userServiceTest.deleteUser(user);

        verify(userRepositoryMock, Mockito.times(1)).delete(user);
    }

    @Test
    public void loadUserListShouldCallTheUserRepositoryFindAllMethodTest() {

        userServiceTest.loadUserList();

        verify(userRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    public void loadUserByIdShouldCallTheUserRepositoryFindByIdMethodTest() {

        when(userRepositoryMock.findById(anyInt())).thenReturn(Optional.of(new User()));

        userServiceTest.loadUserById(anyInt());

        verify(userRepositoryMock, Mockito.times(1)).findById(anyInt());
    }

    @Test
    public void loadUserByIdWithUnknownIdShouldThrowAnExceptionTest() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userServiceTest.loadUserById(2));
        assertEquals("Invalid User Id:" + 2, exception.getMessage());
    }
}
