package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.service.BidListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
public class BidLisIntegrationTest {

    @Autowired
    private BidListService bidListServiceTest;
    @Autowired
    private BidListRepository bidListRepository;

    @BeforeEach
    public void setUpPertest() {
        bidListRepository.deleteAll();
    }


    @Test
    public void createBidListDoesNotExistShouldCreateANewBidListTest() {
        BidList bidList = new BidList();
        bidList.setAccount("account test");
        bidList.setType("type test");
        bidList.setBidQuantity(10d);

        bidListServiceTest.createBidList(bidList);

        assertNotNull(bidListServiceTest.loadBidListList().get(0).getId());
    }

    @Test
    public void updateBidListExistingShouldSaveBidListTest() {
        BidList bidList = new BidList();
        bidList.setAccount("account test");
        bidList.setType("type test");
        bidList.setBidQuantity(10d);

        bidListServiceTest.createBidList(bidList);

        BidList bidListToUpdate = bidListServiceTest.loadBidListList().get(0);

        bidListToUpdate.setAccount("account test update");

        bidListServiceTest.updateBidList(bidListToUpdate);

        assertEquals("account test update",bidListServiceTest.loadBidListList().get(0).getAccount());
    }

    @Test
    public void deleteBidListExistingShouldRemoveBidListTest() {
        BidList bidList = new BidList();
        bidList.setAccount("account test");
        bidList.setType("type test");
        bidList.setBidQuantity(10d);

        bidListServiceTest.createBidList(bidList);

        BidList bidListToDelete = bidListServiceTest.loadBidListList().get(0);

        bidListServiceTest.deleteBidList(bidListToDelete);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> bidListServiceTest.loadBidListById(bidListToDelete.getId()));
        assertEquals("Invalid BidList Id:" + bidListToDelete.getId(), exception.getMessage());
    }


}
