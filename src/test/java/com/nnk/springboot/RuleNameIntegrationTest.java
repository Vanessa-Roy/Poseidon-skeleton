package com.nnk.springboot;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.enums.Role;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.RuleNameService;
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
public class RuleNameIntegrationTest {

    @Autowired
    private RuleNameService ruleNameServiceTest;
    @Autowired
    private UserService userService;
    @Autowired
    private RuleNameRepository ruleNameRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    private int existingRuleNameId;

    @BeforeEach
    public void setUpPertest() {
        ruleNameRepository.deleteAll();
        userRepository.deleteAll();
        User existingUser = new User(null,"fullname Test","passwordTest!0","userTest", Role.USER);
        userService.createUser(existingUser);
        User existingAdmin = new User(null,"fullname Test","passwordTest!0","adminTest", Role.ADMIN);
        userService.createUser(existingAdmin);
        RuleName existingRuleName = new RuleName();
        existingRuleName.setDescription("description test");
        existingRuleName.setJson("json test");
        existingRuleName.setName("name test");
        existingRuleName.setTemplate("template test");
        existingRuleName.setSqlStr("sql str test");
        existingRuleName.setSqlPart("sql part test");
        ruleNameServiceTest.createRuleName(existingRuleName);
        existingRuleNameId = ruleNameServiceTest.loadRuleNameList().get(0).getId();
    }

    @Test
    @WithMockUser(username = "userTest")
    void createRuleNameAsAnUserShouldCreateANewRuleNameTest() throws Exception {
        this.mockMvc
                .perform(post("/ruleName/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("description","description test")
                        .param("json","json test")
                        .param("name","name test")
                        .param("template","template test")
                        .param("sqlStr","sql str test")
                        .param("sqlPart","sql part test")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ruleName/list"));

        assertNotNull(ruleNameServiceTest.loadRuleNameList().get(1).getId());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void createRuleNameAsAnAdminShouldCreateANewRuleNameTest() throws Exception {
        this.mockMvc
                .perform(post("/ruleName/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("description","description test")
                        .param("json","json test")
                        .param("name","name test")
                        .param("template","template test")
                        .param("sqlStr","sql str test")
                        .param("sqlPart","sql part test")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ruleName/list"));

        assertNotNull(ruleNameServiceTest.loadRuleNameList().get(1).getId());
    }

    @Test
    @WithMockUser(username = "userTest")
    void createRuleNameAsAnUserWithWrongParametersShouldNotCreateANewRuleNameTest() throws Exception {
        this.mockMvc
                .perform(post("/ruleName/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("description","description test")
                        .param("json","json test")
                        .param("name","name test")
                        .param("template","template test")
                        .param("sqlStr","sql str test")
                        .param("sqlPart","")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("ruleName/add"));

        assertThrows(IndexOutOfBoundsException.class, () -> ruleNameServiceTest.loadRuleNameList().get(1));
    }

    @Test
    @WithMockUser(username = "userTest")
    void updateRuleNameAsAnUserShouldNotSaveRuleNameTest() throws Exception {
        this.mockMvc
                .perform(post("/ruleName/update/{id}", existingRuleNameId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingRuleNameId))
                        .param("description","description test update")
                        .param("json","json test")
                        .param("name","name test")
                        .param("template","template test")
                        .param("sqlStr","sql str test")
                        .param("sqlPart","sql part test")
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        assertEquals("description test", ruleNameServiceTest.loadRuleNameById(existingRuleNameId).getDescription());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void updateRuleNameAsAnAdminShouldSaveRuleNameTest() throws Exception {
        this.mockMvc
                .perform(post("/ruleName/update/{id}", existingRuleNameId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingRuleNameId))
                        .param("description","description test update")
                        .param("json","json test")
                        .param("name","name test")
                        .param("template","template test")
                        .param("sqlStr","sql str test")
                        .param("sqlPart","sql part test")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ruleName/list"));

        assertEquals("description test update", ruleNameServiceTest.loadRuleNameById(existingRuleNameId).getDescription());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void updateRuleNameAsAnAdminWithWrongParametersShouldNotSaveRuleNameTest() throws Exception {
        this.mockMvc
                .perform(post("/ruleName/update/{id}", existingRuleNameId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingRuleNameId))
                        .param("description","")
                        .param("json","json test")
                        .param("name","name test")
                        .param("template","template test")
                        .param("sqlStr","sql str test")
                        .param("sqlPart","sql part test")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("ruleName/update"));

        assertEquals("description test", ruleNameServiceTest.loadRuleNameById(existingRuleNameId).getDescription());
    }

    @Test
    @WithMockUser(username = "userTest")
    void deleteRuleNameAsAnUserShouldNotRemoveRuleNameTest() throws Exception {
        this.mockMvc
                .perform(post("/ruleName/delete/{id}", existingRuleNameId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingRuleNameId))
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        assertNotNull(ruleNameServiceTest.loadRuleNameById(existingRuleNameId));
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void deleteRuleNameAsAnAdminShouldRemoveRuleNameTest() throws Exception {
        this.mockMvc
                .perform(get("/ruleName/delete/{id}", existingRuleNameId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ruleName/list"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> ruleNameServiceTest.loadRuleNameById(existingRuleNameId));
        assertEquals("Invalid RuleName Id:" + existingRuleNameId, exception.getMessage());
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldAllowAccessToRuleNameListForAuthenticatedUserTest() throws Exception {
        this.mockMvc
                .perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attributeExists("ruleNames"))
                .andExpect(model().attributeExists("isAdmin"));
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldAllowAccessToRuleNameAddForAuthenticatedUserTest() throws Exception {
        this.mockMvc
                .perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(model().attributeExists("ruleName"));
    }

    @Test
    @WithMockUser(username = "userTest", roles={"ADMIN"})
    void shouldAllowAccessToRuleNameUpdateForAdminUserTest() throws Exception {
        this.mockMvc
                .perform(get("/ruleName/update/{id}", existingRuleNameId))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attributeExists("ruleName"));
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldDenyAccessToRuleNameUpdateForNotAdminUserTest() throws Exception {
        this.mockMvc
                .perform(get("/ruleName/update/{id}", existingRuleNameId))
                .andExpect(status().is4xxClientError());
    }


}
