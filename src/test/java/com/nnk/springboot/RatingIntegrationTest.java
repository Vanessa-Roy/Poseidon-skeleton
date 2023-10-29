package com.nnk.springboot;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.enums.Role;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.RatingService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class RatingIntegrationTest {

    @Autowired
    private RatingService ratingServiceTest;
    @Autowired
    private UserService userService;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    private int existingRatingId;

    @BeforeEach
    public void setUpPertest() {
        ratingRepository.deleteAll();
        userRepository.deleteAll();
        User existingUser = new User(null,"fullname Test","passwordTest!0","userTest", Role.USER);
        userService.createUser(existingUser);
        User existingAdmin = new User(null,"fullname Test","passwordTest!0","adminTest", Role.ADMIN);
        userService.createUser(existingAdmin);
        Rating existingRating = new Rating();
        existingRating.setFitchRating("fitchRating test");
        existingRating.setSandPRating("sandPRating test");
        existingRating.setMoodysRating("moodysRating test");
        existingRating.setOrderNumber(1);
        ratingServiceTest.createRating(existingRating);
        existingRatingId = ratingServiceTest.loadRatingList().get(0).getId();
    }

    @Test
    @WithMockUser(username = "userTest")
    void createRatingAsAnUserShouldCreateANewRatingTest() throws Exception {
        this.mockMvc
                .perform(post("/rating/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fitchRating","fitchRating test")
                        .param("sandPRating","sandPRating test")
                        .param("moodysRating","moodysRating test")
                        .param("orderNumber","2")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/rating/list"));

        assertNotNull(ratingServiceTest.loadRatingList().get(1).getId());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void createRatingAsAnAdminShouldCreateANewRatingTest() throws Exception {
        this.mockMvc
                .perform(post("/rating/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fitchRating","fitchRating test")
                        .param("sandPRating","sandPRating test")
                        .param("moodysRating","moodysRating test")
                        .param("orderNumber","2")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/rating/list"));

        assertNotNull(ratingServiceTest.loadRatingList().get(1).getId());
    }

    @Test
    @WithMockUser(username = "userTest")
    void createRatingAsAnUserWithWrongParametersShouldNotCreateANewRatingTest() throws Exception {
        this.mockMvc
                .perform(post("/rating/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fitchRating","fitchRating test")
                        .param("sandPRating","sandPRating test")
                        .param("moodysRating","moodysRating test")
                        .param("orderNumber","wrong order number")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("rating/add"));

        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> ratingServiceTest.loadRatingList().get(1));
    }

    @Test
    @WithMockUser(username = "userTest")
    void updateRatingAsAnUserShouldNotSaveRatingTest() throws Exception {
        this.mockMvc
                .perform(post("/rating/update/{id}", existingRatingId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingRatingId))
                        .param("fitchRating","fitchRating test")
                        .param("sandPRating","sandPRating test")
                        .param("moodysRating","moodysRating test")
                        .param("orderNumber","2")
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        assertEquals(1, ratingServiceTest.loadRatingById(existingRatingId).getOrderNumber());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void updateRatingAsAnAdminShouldSaveRatingTest() throws Exception {
        this.mockMvc
                .perform(post("/rating/update/{id}", existingRatingId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingRatingId))
                        .param("fitchRating","fitchRating test")
                        .param("sandPRating","sandPRating test")
                        .param("moodysRating","moodysRating test")
                        .param("orderNumber","2")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/rating/list"));

        assertEquals(2, ratingServiceTest.loadRatingById(existingRatingId).getOrderNumber());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void updateRatingAsAnAdminWithWrongParametersShouldNotSaveRatingTest() throws Exception {
        this.mockMvc
                .perform(post("/rating/update/{id}", existingRatingId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingRatingId))
                        .param("fitchRating","fitchRating test")
                        .param("sandPRating","sandPRating test")
                        .param("moodysRating","moodysRating test")
                        .param("orderNumber","wrong order number")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("rating/update"));

        assertEquals(1, ratingServiceTest.loadRatingById(existingRatingId).getOrderNumber());
    }

    @Test
    @WithMockUser(username = "userTest")
    void deleteRatingAsAnUserShouldNotRemoveRatingTest() throws Exception {
        this.mockMvc
                .perform(post("/rating/delete/{id}", existingRatingId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingRatingId))
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        assertNotNull(ratingServiceTest.loadRatingById(existingRatingId));
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void deleteRatingAsAnAdminShouldRemoveRatingTest() throws Exception {
        this.mockMvc
                .perform(get("/rating/delete/{id}", existingRatingId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/rating/list"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> ratingServiceTest.loadRatingById(existingRatingId));
        assertEquals("Invalid Rating Id:" + existingRatingId, exception.getMessage());
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldAllowAccessToRatingListForAuthenticatedUserTest() throws Exception {
        this.mockMvc
                .perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"))
                .andExpect(model().attributeExists("isAdmin"));
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldAllowAccessToRatingAddForAuthenticatedUserTest() throws Exception {
        this.mockMvc
                .perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().attributeExists("rating"));
    }

    @Test
    @WithMockUser(username = "userTest", roles={"ADMIN"})
    void shouldAllowAccessToRatingUpdateForAdminUserTest() throws Exception {
        this.mockMvc
                .perform(get("/rating/update/{id}", existingRatingId))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attributeExists("rating"));
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldDenyAccessToRatingUpdateForNotAdminUserTest() throws Exception {
        this.mockMvc
                .perform(get("/rating/update/{id}", existingRatingId))
                .andExpect(status().is4xxClientError());
    }


}
