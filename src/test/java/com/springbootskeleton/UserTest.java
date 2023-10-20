package com.springbootskeleton;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.enums.Role;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @InjectMocks
    UserService userServiceTest;

    @Mock
    UserRepository userRepositoryMock;

    private final User user = new User(1,"fullnameTest","Password0!","usernameTest", Role.USER);

    @Test
    public void createUserDoesNotExistShouldCallTheUserRepositorySaveMethodTest() throws Exception {

        userServiceTest.createUser(user);

        verify(userRepositoryMock, Mockito.times(1)).save(user);
    }

    @Test
    public void updateUserShouldCallTheUserRepositorySaveMethodTest() {

        userServiceTest.updateUser(user);

        verify(userRepositoryMock, Mockito.times(1)).save(user);
    }

    @Test
    public void deleteUserShouldCallTheUserRepositoryDeleteMethodTest() {

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
