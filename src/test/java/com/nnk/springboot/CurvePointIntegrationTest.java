package com.nnk.springboot;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.enums.Role;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.CurvePointService;
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
public class CurvePointIntegrationTest {

    @Autowired
    private CurvePointService curvePointServiceTest;
    @Autowired
    private UserService userService;
    @Autowired
    private CurvePointRepository curvePointRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    private int existingCurvePointId;

    @BeforeEach
    public void setUpPertest() {
        curvePointRepository.deleteAll();
        userRepository.deleteAll();
        User existingUser = new User(null,"fullname Test","passwordTest!0","userTest", Role.USER);
        userService.createUser(existingUser);
        User existingAdmin = new User(null,"fullname Test","passwordTest!0","adminTest", Role.ADMIN);
        userService.createUser(existingAdmin);
        CurvePoint existingCurvePoint = new CurvePoint();
        existingCurvePoint.setCurveId(1);
        existingCurvePoint.setValue(1d);
        existingCurvePoint.setTerm(1d);
        curvePointServiceTest.createCurvePoint(existingCurvePoint);
        existingCurvePointId = curvePointServiceTest.loadCurvePointList().get(0).getId();
    }

    @Test
    @WithMockUser(username = "userTest")
    void createCurvePointAsAnUserShouldCreateANewCurvePointTest() throws Exception {
        this.mockMvc
                .perform(post("/curvePoint/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("curveId","1")
                        .param("value","1d")
                        .param("term","1d")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/curvePoint/list"));

        assertNotNull(curvePointServiceTest.loadCurvePointList().get(1).getId());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void createCurvePointAsAnAdminShouldCreateANewCurvePointTest() throws Exception {
        this.mockMvc
                .perform(post("/curvePoint/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("curveId","1")
                        .param("value","1d")
                        .param("term","1d")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/curvePoint/list"));

        assertNotNull(curvePointServiceTest.loadCurvePointList().get(1).getId());
    }

    @Test
    @WithMockUser(username = "userTest")
    void createCurvePointAsAnUserWithWrongParametersShouldNotCreateANewCurvePointTest() throws Exception {
        this.mockMvc
                .perform(post("/curvePoint/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("curveId","1")
                        .param("value","1d")
                        .param("term","wrong term")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("curvePoint/add"));

        assertThrows(IndexOutOfBoundsException.class, () -> curvePointServiceTest.loadCurvePointList().get(1));
    }

    @Test
    @WithMockUser(username = "userTest")
    void updateCurvePointAsAnUserShouldNotSaveCurvePointTest() throws Exception {
        this.mockMvc
                .perform(post("/curvePoint/update/{id}", existingCurvePointId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingCurvePointId))
                        .param("curveId","2")
                        .param("value","1d")
                        .param("term","2d")
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        assertEquals(1d, curvePointServiceTest.loadCurvePointById(existingCurvePointId).getTerm());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void updateCurvePointAsAnAdminShouldSaveCurvePointTest() throws Exception {
        this.mockMvc
                .perform(post("/curvePoint/update/{id}", existingCurvePointId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingCurvePointId))
                        .param("curveId","1")
                        .param("value","1d")
                        .param("term","2d")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/curvePoint/list"));

        assertEquals(2d, curvePointServiceTest.loadCurvePointById(existingCurvePointId).getTerm());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void updateCurvePointAsAnAdminWithWrongParametersShouldNotSaveCurvePointTest() throws Exception {
        this.mockMvc
                .perform(post("/curvePoint/update/{id}", existingCurvePointId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingCurvePointId))
                        .param("curveId","1")
                        .param("value","1d")
                        .param("term","wrong term")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("curvePoint/update"));

        assertEquals(1d, curvePointServiceTest.loadCurvePointById(existingCurvePointId).getTerm());
    }

    @Test
    @WithMockUser(username = "userTest")
    void deleteCurvePointAsAnUserShouldNotRemoveCurvePointTest() throws Exception {
        this.mockMvc
                .perform(post("/curvePoint/delete/{id}", existingCurvePointId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingCurvePointId))
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        assertNotNull(curvePointServiceTest.loadCurvePointById(existingCurvePointId));
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void deleteCurvePointAsAnAdminShouldRemoveCurvePointTest() throws Exception {
        this.mockMvc
                .perform(get("/curvePoint/delete/{id}", existingCurvePointId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/curvePoint/list"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> curvePointServiceTest.loadCurvePointById(existingCurvePointId));
        assertEquals("Invalid Curve Point Id:" + existingCurvePointId, exception.getMessage());
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldAllowAccessToCurvePointListForAuthenticatedUserTest() throws Exception {
        this.mockMvc
                .perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(model().attributeExists("isAdmin"));
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldAllowAccessToCurvePointAddForAuthenticatedUserTest() throws Exception {
        this.mockMvc
                .perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeExists("curvePoint"));
    }

    @Test
    @WithMockUser(username = "userTest", roles={"ADMIN"})
    void shouldAllowAccessToCurvePointUpdateForAdminUserTest() throws Exception {
        this.mockMvc
                .perform(get("/curvePoint/update/{id}", existingCurvePointId))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeExists("curvePoint"));
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldDenyAccessToCurvePointUpdateForNotAdminUserTest() throws Exception {
        this.mockMvc
                .perform(get("/curvePoint/update/{id}", existingCurvePointId))
                .andExpect(status().is4xxClientError());
    }


}
