package com.springbootskeleton;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.service.TradeService;
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
public class TradeTest {

    @InjectMocks
    TradeService tradeServiceTest;

    @Mock
    TradeRepository tradeRepositoryMock;

    private TradeDto tradeDto;

    private Trade trade;


    private final Timestamp creationDate = Timestamp.from(Instant.now());

    @Test
    public void saveTradeDoesNotExistShouldCallTheTradeRepositorySaveMethodTest() {

        tradeDto = new TradeDto(null, "account", "type", 1.0);

        tradeServiceTest.saveTrade(tradeDto);

        verify(tradeRepositoryMock, Mockito.times(1)).save(any(Trade.class));
    }

    @Test
    public void updateTradeShouldCallTheTradeRepositorySaveMethodTest() {

        trade = new Trade(1, "account", "type", 1.0, creationDate);

        tradeDto = new TradeDto(1, "account", "type", 3.3);

        tradeServiceTest.updateTrade(trade, tradeDto);

        verify(tradeRepositoryMock, Mockito.times(1)).save(trade);
    }

    @Test
    public void deleteTradeShouldCallTheTradeRepositoryDeleteMethodTest() {

        trade = new Trade();

        tradeServiceTest.deleteTrade(trade);

        verify(tradeRepositoryMock, Mockito.times(1)).delete(trade);
    }

    @Test
    public void loadTradeDtoListShouldReturnAllTheTradesDtoTest() {

        List<Trade> tradeList = new ArrayList<>(List.of(new Trade(1, "account", "type", 1.0, creationDate)));

        when(tradeRepositoryMock.findAll()).thenReturn(tradeList);

        List<TradeDto> tradeDtoList = tradeServiceTest.loadTradeDtoList();

        verify(tradeRepositoryMock, Mockito.times(1)).findAll();
        assertEquals(tradeList.get(0).getId(), tradeDtoList.get(0).getId());
        assertEquals(tradeList.get(0).getAccount(), tradeDtoList.get(0).getAccount());
        assertEquals(tradeList.get(0).getType(), tradeDtoList.get(0).getType());
        assertEquals(tradeList.get(0).getBuyQuantity(), tradeDtoList.get(0).getBuyQuantity());
    }

    @Test
    public void loadTradeDtoByIdShouldReturnATradeDtoTest() {

        Trade trade = new Trade(1, "account", "type", 1.0, creationDate);

        when(tradeRepositoryMock.findById(trade.getId())).thenReturn(Optional.of(trade));

        TradeDto tradeDto = tradeServiceTest.loadTradeDtoById(trade.getId());

        verify(tradeRepositoryMock, Mockito.times(1)).findById(trade.getId());
        assertEquals(trade.getId(), tradeDto.getId());
        assertEquals(trade.getAccount(), tradeDto.getAccount());
        assertEquals(trade.getType(), tradeDto.getType());
        assertEquals(trade.getBuyQuantity(), tradeDto.getBuyQuantity());
    }

    @Test
    public void loadTradeDtoByIdWithUnknownIdShouldThrowAnExceptionTest() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> tradeServiceTest.loadTradeDtoById(2));
        assertEquals("Invalid Trade Id:" + 2, exception.getMessage());
    }

    @Test
    public void mapToTradeDTOShouldReturnATradeDtoFromATradeEntityTest() {

        trade = new Trade(1, "account", "type", 1.0, creationDate);

        TradeDto tradeDto = tradeServiceTest.mapToTradeDTO(trade);

        assertEquals(trade.getId(), tradeDto.getId());
        assertEquals(trade.getAccount(), tradeDto.getAccount());
        assertEquals(trade.getType(), tradeDto.getType());
        assertEquals(trade.getBuyQuantity(), tradeDto.getBuyQuantity());
    }
}
