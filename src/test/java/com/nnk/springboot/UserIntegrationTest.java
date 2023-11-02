package com.nnk.springboot;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.enums.Role;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserIntegrationTest {
    @Autowired
    private UserService userServiceTest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    private int existingUserId;

    @BeforeEach
    public void setUpPertest() {
        userRepository.deleteAll();
        User existingUser = new User(null,"fullname test","passwordTest!0","userTest", Role.USER);
        userServiceTest.createUser(existingUser);
        User existingAdmin = new User(null,"fullname test","passwordTest!0","adminTest", Role.ADMIN);
        userServiceTest.createUser(existingAdmin);
        existingUserId = userServiceTest.loadUserByUsername("userTest").getId();
    }

    @Test
    void createAnUserAsAnonymousShouldNotCreateANewUserTest() throws Exception {
        this.mockMvc
                .perform(post("/user/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullname","fullname test")
                        .param("password","passwordTest!0")
                        .param("username","newUserTest")
                        .param("role","USER")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        assertNull(userServiceTest.loadUserByUsername("newUserTest"));
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void createAnUserAsAdminShouldCreateANewUserTest() throws Exception {
        this.mockMvc
                .perform(post("/user/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullname","fullname test")
                        .param("password","passwordTest!0")
                        .param("username","newUserTest")
                        .param("role","USER")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));

        assertNotNull(userServiceTest.loadUserByUsername("newUserTest"));
        assertEquals(Role.USER,userServiceTest.loadUserByUsername("newUserTest").getRole());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void createAnUserAsAdminWithWrongParametersShouldNotCreateANewUserTest() throws Exception {
        this.mockMvc
                .perform(post("/user/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullname","fullname test")
                        .param("password","")
                        .param("username","newUserTest")
                        .param("role","USER")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/add"));

        assertNull(userServiceTest.loadUserByUsername("newUserTest"));
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void createAnUserAsAdminWithUserAlreadyExistingShouldNotCreateANewUserTest() throws Exception {
        this.mockMvc
                .perform(post("/user/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullname","fullname test")
                        .param("password","passwordTest!0")
                        .param("username","userTest")
                        .param("role","USER")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/add"));
    }
    @Test
    @WithMockUser(username = "userTest")
    void createAnUserAsUserShouldNotCreateANewUserTest() throws Exception {
        this.mockMvc
                .perform(post("/user/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullname","fullname test")
                        .param("password","passwordTest!0")
                        .param("username","newUserTest")
                        .param("role","USER")
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        assertNull(userServiceTest.loadUserByUsername("newUserTest"));
    }


    @Test
    @WithMockUser(username = "userTest")
    void updateUserAsAnUserShouldNotSaveUserTest() throws Exception {
        this.mockMvc
                .perform(post("/user/update/{id}", existingUserId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingUserId))
                        .param("fullname","fullname test update")
                        .param("password","passwordTest!0")
                        .param("username","newUserTest")
                        .param("role","USER")
                        .with(csrf()))
                .andExpect(status().is4xxClientError());
        assertEquals("fullname test", userServiceTest.loadUserById(existingUserId).getFullname());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void updateUserAsAnAdminShouldSaveUserTest() throws Exception {
        this.mockMvc
                .perform(post("/user/update/{id}", existingUserId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingUserId))
                        .param("fullname","fullname test update")
                        .param("password","passwordTest!0")
                        .param("username","newUserTest")
                        .param("role","USER")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));
        assertEquals("fullname test update", userServiceTest.loadUserById(existingUserId).getFullname());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void updateUserAsAnAdminWithWrongParametersShouldNotSaveUserTest() throws Exception {
        this.mockMvc
                .perform(post("/user/update/{id}", existingUserId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingUserId))
                        .param("fullname","")
                        .param("password","passwordTest!0")
                        .param("username","newUserTest")
                        .param("role","USER")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/update"));
        assertEquals("fullname test", userServiceTest.loadUserById(existingUserId).getFullname());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void updateUserAsAnAdminWithNewUsernameAlreadyExistingShouldNotSaveUserTest() throws Exception {
        this.mockMvc
                .perform(post("/user/update/{id}", existingUserId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingUserId))
                        .param("fullname","fullname test")
                        .param("password","passwordTest!0")
                        .param("username","adminTest")
                        .param("role","USER")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("errorMessage"));
        assertEquals("userTest", userServiceTest.loadUserById(existingUserId).getUsername());
    }

    @Test
    @WithMockUser(username = "userTest")
    void deleteUserAsAnUserShouldNotRemoveUserTest() throws Exception {
        this.mockMvc
                .perform(post("/user/delete/{id}", existingUserId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingUserId))
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        assertNotNull(userServiceTest.loadUserById(existingUserId));
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void deleteUserAsAnAdminShouldRemoveUserTest() throws Exception {
        this.mockMvc
                .perform(get("/user/delete/{id}", existingUserId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userServiceTest.loadUserById(existingUserId));
        assertEquals("Invalid User Id:" + existingUserId, exception.getMessage());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void shouldAllowAccessToUserListForAdminUserTest() throws Exception {
        this.mockMvc
                .perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldDenyAccessToUserListForNotAdminUserTest() throws Exception {
        this.mockMvc
                .perform(get("/user/list"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldDenyAccessToUserListForAnonymousUserTest() throws Exception {
        this.mockMvc
                .perform(get("/user/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldDenyAccessToUserAddForNotAdminUserTest() throws Exception {
        this.mockMvc
                .perform(get("/user/add"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "userTest", roles={"ADMIN"})
    void shouldAllowAccessToUserAddForAdminUserTest() throws Exception {
        this.mockMvc
                .perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void shouldDenyAccessToUserAddForAnonymousUserTest() throws Exception {
        this.mockMvc
                .perform(get("/user/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "userTest", roles={"ADMIN"})
    void shouldAllowAccessToUserUpdateForAdminUserTest() throws Exception {
        this.mockMvc
                .perform(get("/user/update/{id}", existingUserId))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldDenyAccessToUserUpdateForNotAdminUserTest() throws Exception {
        this.mockMvc
                .perform(get("/user/update/{id}", existingUserId))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldAllowAccessToLoginForAnonymousUserTest() throws Exception {
        this.mockMvc
                .perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void shouldAllowAccessToHomeForAnonymousUserTest() throws Exception {
        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldAllowAccessToHomeForUserTest() throws Exception {
        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    @WithMockUser(username = "userTest", roles={"ADMIN"})
    void shouldAllowAccessToHomeForAdminUserTest() throws Exception {
        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    @WithMockUser(username = "userTest", roles={"ADMIN"})
    void shouldAllowAccessToHomeButtonForAdminUserTest() throws Exception {
        this.mockMvc
                .perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"));
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldAllowAccessToHomeButtonForUserTest() throws Exception {
        this.mockMvc
                .perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"));
    }

    @Test
    void shouldDenyAccessToHomeButtonForAnonymousUserTest() throws Exception {
        this.mockMvc
                .perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void shouldAccessToUserListAfterLoginAsAdminUserTest() throws Exception {
        RequestBuilder requestBuilder = formLogin().user("adminTest").password("passwordTest!0");
        this.mockMvc
                .perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
    }

    @Test
    void shouldAccessToHomeAfterLoginAsUserTest() throws Exception {
        RequestBuilder requestBuilder = formLogin().user("userTest").password("passwordTest!0");
        this.mockMvc
                .perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

}
