package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.enums.Role;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.BidListService;
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
public class BidListIntegrationTest {

    @Autowired
    private BidListService bidListServiceTest;
    @Autowired
    private UserService userService;
    @Autowired
    private BidListRepository bidListRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    private int existingBidListId;

    @BeforeEach
    public void setUpPertest() {
        bidListRepository.deleteAll();
        userRepository.deleteAll();
        User existingUser = new User(null,"fullname Test","passwordTest!0","userTest", Role.USER);
        userService.createUser(existingUser);
        User existingAdmin = new User(null,"fullname Test","passwordTest!0","adminTest", Role.ADMIN);
        userService.createUser(existingAdmin);
        BidList existingBidList = new BidList();
        existingBidList.setAccount("account test");
        existingBidList.setType("type test");
        existingBidList.setBidQuantity(10d);
        bidListServiceTest.createBidList(existingBidList);
        existingBidListId = bidListServiceTest.loadBidListList().get(0).getId();
    }

    @Test
    @WithMockUser(username = "userTest")
    void createBidListAsAnUserShouldCreateANewBidListTest() throws Exception {
        this.mockMvc
                .perform(post("/bidList/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account","account test")
                        .param("type","type test")
                        .param("bidQuantity","10d")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"));

        assertNotNull(bidListServiceTest.loadBidListList().get(1).getId());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void createBidListAsAnAdminShouldCreateANewBidListTest() throws Exception {
        this.mockMvc
                .perform(post("/bidList/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account","account test")
                        .param("type","type test")
                        .param("bidQuantity","10d")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"));

        assertNotNull(bidListServiceTest.loadBidListList().get(1).getId());
    }

    @Test
    @WithMockUser(username = "userTest")
    void createBidListAsAnUserWithWrongParametersShouldNotCreateANewBidListTest() throws Exception {
        this.mockMvc
                .perform(post("/bidList/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account","account test")
                        .param("type","type test")
                        .param("bidQuantity","wrong Bid Quantity")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("bidList/add"));

        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> bidListServiceTest.loadBidListList().get(1));
    }

    @Test
    @WithMockUser(username = "userTest")
    void updateBidListAsAnUserShouldNotSaveBidListTest() throws Exception {
        this.mockMvc
                .perform(post("/bidList/update/{id}", existingBidListId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingBidListId))
                        .param("account","account test")
                        .param("type","type test")
                        .param("bidQuantity","10d")
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        assertEquals("account test", bidListServiceTest.loadBidListById(existingBidListId).getAccount());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void updateBidListAsAnAdminShouldSaveBidListTest() throws Exception {
        this.mockMvc
                .perform(post("/bidList/update/{id}", existingBidListId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingBidListId))
                        .param("account","account test update")
                        .param("type","type test")
                        .param("bidQuantity","10d")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"));

        assertEquals("account test update", bidListServiceTest.loadBidListById(existingBidListId).getAccount());
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void updateBidListAsAnAdminWithWrongParametersShouldNotSaveBidListTest() throws Exception {
        this.mockMvc
                .perform(post("/bidList/update/{id}", existingBidListId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingBidListId))
                        .param("account","account test update")
                        .param("type","type test")
                        .param("bidQuantity","wrong Bid Quantity")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("bidList/update"));

        assertEquals(10d, bidListServiceTest.loadBidListById(existingBidListId).getBidQuantity());
    }

    @Test
    @WithMockUser(username = "userTest")
    void deleteBidListAsAnUserShouldNotRemoveBidListTest() throws Exception {
        this.mockMvc
                .perform(post("/bidList/delete/{id}", existingBidListId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(existingBidListId))
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        assertNotNull(bidListServiceTest.loadBidListById(existingBidListId));
    }

    @Test
    @WithMockUser(username = "adminTest", roles={"ADMIN"})
    void deleteBidListAsAnAdminShouldRemoveBidListTest() throws Exception {
        this.mockMvc
                .perform(get("/bidList/delete/{id}", existingBidListId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> bidListServiceTest.loadBidListById(existingBidListId));
        assertEquals("Invalid BidList Id:" + existingBidListId, exception.getMessage());
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldAllowAccessToBidListListForAuthenticatedUserTest() throws Exception {
        this.mockMvc
                .perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeExists("bidLists"))
                .andExpect(model().attributeExists("isAdmin"));
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldAllowAccessToBidListAddForAuthenticatedUserTest() throws Exception {
        this.mockMvc
                .perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeExists("bidList"));
    }

    @Test
    @WithMockUser(username = "userTest", roles={"ADMIN"})
    void shouldAllowAccessToBidListUpdateForAdminUserTest() throws Exception {
        this.mockMvc
                .perform(get("/bidList/update/{id}", existingBidListId))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attributeExists("bidList"));
    }

    @Test
    @WithMockUser(username = "userTest")
    void shouldDenyAccessToBidListUpdateForNotAdminUserTest() throws Exception {
        this.mockMvc
                .perform(get("/bidList/update/{id}", existingBidListId))
                .andExpect(status().is4xxClientError());
    }


}
