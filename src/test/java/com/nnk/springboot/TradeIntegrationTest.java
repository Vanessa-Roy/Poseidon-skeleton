package com.nnk.springboot;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.enums.Role;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.TradeService;
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
public class TradeIntegrationTest {

    @Autowired
    private TradeService tradeServiceTest;
    @Autowired
    private UserService userService;
    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    private int existingTradeId;

    @BeforeEach
    public void setUpPertest() {
        tradeRepository.deleteAll();
        userRepository.deleteAll();
        User existingUser = new User(null,"fullname Test","passwordTest!0","userTest", Role.USER);
        userService.createUser(existingUser);
        User existingAdmin = new User(null,"fullname Test","passwordTest!0","adminTest", Role.ADMIN);
        userService.createUser(existingAdmin);
        Trade existingTrade = new Trade();
        existingTrade.setAccount("account test");
        existingTrade.setType("type test");
        existingTrade.setBuyQuantity(10d);
        tradeServiceTest.createTrade(existingTrade);
        existingTradeId = tradeServiceTest.loadTradeList().get(0).getId();
    }

    @Test
    @WithMockUser(username = "userTest")
    void createTradeAsAnUserShouldCreateANewTradeTest() throws Exception {
        this.mockMvc
                .perform(post("/trade/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account","account test")
                        .param("type","type test")
                        .param("buyQuantity","10d")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));

        assertNotNull(tradeServiceTest.loadTradeList().get(1).getId());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void createTradeAsAnAdminShouldCreateANewTradeTest() throws Exception {
        this.mockMvc
                .perform(post("/trade/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account","account test")
                        .param("type","type test")
                        .param("buyQuantity","10d")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));

        assertNotNull(tradeServiceTest.loadTradeList().get(1).getId());
    }

    @Test
    @WithMockUser(username = "userTest")
    void createTradeAsAnUserWithWrongParametersShouldNotCreateANewTradeTest() throws Exception {
        this.mockMvc
                .perform(post("/trade/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account","account test")
                        .param("type","type test")
                        .param("buyQuantity","wrong Bid Quantity")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("trade/add"));

        assertThrows(IndexOutOfBoundsException.class, () -> tradeServiceTest.loadTradeList().get(1));
    }

    @Test
    @WithMockUser(username = "userTest")
    void updateTradeAsAnUserShouldNotSaveTradeTest() throws Exception {
        this.mockMvc
                .perform(post("/trade/update/{id}", existingTradeId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingTradeId))
                        .param("account","account test update")
                        .param("type","type test")
                        .param("buyQuantity","10d")
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        assertEquals("account test", tradeServiceTest.loadTradeById(existingTradeId).getAccount());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void updateTradeAsAnAdminShouldSaveTradeTest() throws Exception {
        this.mockMvc
                .perform(post("/trade/update/{id}", existingTradeId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingTradeId))
                        .param("account","account test update")
                        .param("type","type test")
                        .param("buyQuantity","10d")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));

        assertEquals("account test update", tradeServiceTest.loadTradeById(existingTradeId).getAccount());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void updateTradeAsAnAdminWithWrongParametersShouldNotSaveTradeTest() throws Exception {
        this.mockMvc
                .perform(post("/trade/update/{id}", existingTradeId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingTradeId))
                        .param("account","account test update")
                        .param("type","type test")
                        .param("buyQuantity","wrong Bid Quantity")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("trade/update"));

        assertEquals(10d, tradeServiceTest.loadTradeById(existingTradeId).getBuyQuantity());
    }

    @Test
    @WithMockUser(username = "userTest")
    void deleteTradeAsAnUserShouldNotRemoveTradeTest() throws Exception {
        this.mockMvc
                .perform(post("/trade/delete/{id}", existingTradeId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingTradeId))
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        assertNotNull(tradeServiceTest.loadTradeById(existingTradeId));
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void deleteTradeAsAnAdminShouldRemoveTradeTest() throws Exception {
        this.mockMvc
                .perform(get("/trade/delete/{id}", existingTradeId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> tradeServiceTest.loadTradeById(existingTradeId));
        assertEquals("Invalid Trade Id:" + existingTradeId, exception.getMessage());
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldAllowAccessToTradeListForAuthenticatedUserTest() throws Exception {
        this.mockMvc
                .perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("trades"))
                .andExpect(model().attributeExists("isAdmin"));
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldAllowAccessToTradeAddForAuthenticatedUserTest() throws Exception {
        this.mockMvc
                .perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeExists("trade"));
    }

    @Test
    @WithMockUser(username = "userTest", roles={"ADMIN"})
    void shouldAllowAccessToTradeUpdateForAdminUserTest() throws Exception {
        this.mockMvc
                .perform(get("/trade/update/{id}", existingTradeId))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeExists("trade"));
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldDenyAccessToTradeUpdateForNotAdminUserTest() throws Exception {
        this.mockMvc
                .perform(get("/trade/update/{id}", existingTradeId))
                .andExpect(status().is4xxClientError());
    }


}
