package com.springbootskeleton;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.service.BidListService;
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
public class BidListTest {

    @InjectMocks
    BidListService bidListServiceTest;

    @Mock
    BidListRepository bidListRepositoryMock;

    private final BidList bidList = new BidList();

    @Test
    public void createBidListDoesNotExistShouldCallTheBidListRepositorySaveMethodTest() {

        bidListServiceTest.createBidList(bidList);

        verify(bidListRepositoryMock, Mockito.times(1)).save(bidList);
    }

    @Test
    public void updateBidListShouldCallTheBidListRepositorySaveMethodTest() {

        bidListServiceTest.updateBidList(bidList);

        verify(bidListRepositoryMock, Mockito.times(1)).save(bidList);
    }

    @Test
    public void deleteBidListShouldCallTheBidListRepositoryDeleteMethodTest() {

        bidListServiceTest.deleteBidList(bidList);

        verify(bidListRepositoryMock, Mockito.times(1)).delete(bidList);
    }

    @Test
    public void loadBidListListShouldCallTheBidListRepositoryFindAllMethodTest() {

        bidListServiceTest.loadBidListList();

        verify(bidListRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    public void loadBidListByIdShouldCallTheBidListRepositoryFindByIdMethodTest() {

        when(bidListRepositoryMock.findById(anyInt())).thenReturn(Optional.of(bidList));

        bidListServiceTest.loadBidListById(anyInt());

        verify(bidListRepositoryMock, Mockito.times(1)).findById(anyInt());
    }

    @Test
    public void loadBidListByIdWithUnknownIdShouldThrowAnExceptionTest() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> bidListServiceTest.loadBidListById(2));
        assertEquals("Invalid BidList Id:" + 2, exception.getMessage());
    }
}
