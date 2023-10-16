package com.springbootskeleton;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.service.BidListService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BidListTest {

    @InjectMocks
    BidListService bidListServiceTest;

    @Mock
    BidListRepository bidListRepositoryMock;

    private BidListDto bidListDto;

    private BidList bidList;


    private final Timestamp creationDate = Timestamp.from(Instant.now());

    @Test
    public void saveBidListDoesNotExistShouldCallTheBidListRepositorySaveMethodTest() {

        bidListDto = new BidListDto(null, "account", "type", 1.0);

        bidListServiceTest.saveBidList(bidListDto);

        verify(bidListRepositoryMock, Mockito.times(1)).save(any(BidList.class));
    }

    @Test
    public void updateBidListShouldCallTheBidListRepositorySaveMethodTest() {

        bidList = new BidList(1, "account", "type", 1.0, creationDate);

        bidListDto = new BidListDto(1, "account", "type", 3.3);

        bidListServiceTest.updateBidList(bidList, bidListDto);

        verify(bidListRepositoryMock, Mockito.times(1)).save(bidList);
    }

    @Test
    public void deleteBidListShouldCallTheBidListRepositoryDeleteMethodTest() {

        bidList = new BidList();

        bidListServiceTest.deleteBidList(bidList);

        verify(bidListRepositoryMock, Mockito.times(1)).delete(bidList);
    }

    @Test
    public void loadBidListDtoListShouldReturnAllTheBidListsDtoTest() {

        List<BidList> bidListList = new ArrayList<>(List.of(new BidList(1, "account", "type", 1.0, creationDate)));

        when(bidListRepositoryMock.findAll()).thenReturn(bidListList);

        List<BidListDto> bidListDtoList = bidListServiceTest.loadBidListDtoList();

        verify(bidListRepositoryMock, Mockito.times(1)).findAll();
        assertEquals(bidListList.get(0).getId(), bidListDtoList.get(0).getId());
        assertEquals(bidListList.get(0).getAccount(), bidListDtoList.get(0).getAccount());
        assertEquals(bidListList.get(0).getType(), bidListDtoList.get(0).getType());
        assertEquals(bidListList.get(0).getBidQuantity(), bidListDtoList.get(0).getBidQuantity());
    }

    @Test
    public void loadBidListDtoByIdShouldReturnABidListDtoTest() {

        BidList bidList = new BidList(1, "account", "type", 1.0, creationDate);

        when(bidListRepositoryMock.findById(bidList.getId())).thenReturn(Optional.of(bidList));

        BidListDto bidListDto = bidListServiceTest.loadBidListDtoById(bidList.getId());

        verify(bidListRepositoryMock, Mockito.times(1)).findById(bidList.getId());
        assertEquals(bidList.getId(), bidListDto.getId());
        assertEquals(bidList.getAccount(), bidListDto.getAccount());
        assertEquals(bidList.getType(), bidListDto.getType());
        assertEquals(bidList.getBidQuantity(), bidListDto.getBidQuantity());
    }

    @Test
    public void loadBidListDtoByIdWithUnknownIdShouldThrowAnExceptionTest() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> bidListServiceTest.loadBidListDtoById(2));
        assertEquals("Invalid BidList Id:" + 2, exception.getMessage());
    }

    @Test
    public void mapToBidListDTOShouldReturnABidListDtoFromABidListEntityTest() {

        bidList = new BidList(1, "account", "type", 1.0, creationDate);

        BidListDto bidListDto = bidListServiceTest.mapToBidListDTO(bidList);

        assertEquals(bidList.getId(), bidListDto.getId());
        assertEquals(bidList.getAccount(), bidListDto.getAccount());
        assertEquals(bidList.getType(), bidListDto.getType());
        assertEquals(bidList.getBidQuantity(), bidListDto.getBidQuantity());
    }
}
