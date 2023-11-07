package com.nnk.springboot;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.service.TradeService;
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
public class TradeTest {

    @InjectMocks
    TradeService tradeServiceTest;

    @Mock
    TradeRepository tradeRepositoryMock;

    private final Trade trade = new Trade();

    @Test
    public void createTradeDoesNotExistShouldCallTheTradeRepositorySaveMethodTest() {

        tradeServiceTest.createTrade(trade);

        verify(tradeRepositoryMock, Mockito.times(1)).save(trade);
    }

    @Test
    public void updateTradeShouldCallTheTradeRepositorySaveMethodTest() {

        tradeServiceTest.updateTrade(trade);

        verify(tradeRepositoryMock, Mockito.times(1)).save(trade);
    }

    @Test
    public void deleteTradeShouldCallTheTradeRepositoryDeleteMethodTest() {

        tradeServiceTest.deleteTrade(trade);

        verify(tradeRepositoryMock, Mockito.times(1)).delete(trade);
    }

    @Test
    public void loadTradeListShouldCallTheTradeRepositoryFindAllMethodTest() {

        tradeServiceTest.loadTradeList();

        verify(tradeRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    public void loadTradeByIdShouldCallTheTradeRepositoryFindByIdMethodTest() {

        when(tradeRepositoryMock.findById(anyInt())).thenReturn(Optional.of(trade));

        tradeServiceTest.loadTradeById(anyInt());

        verify(tradeRepositoryMock, Mockito.times(1)).findById(anyInt());
    }

    @Test
    public void loadTradeByIdWithUnknownIdShouldThrowAnExceptionTest() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> tradeServiceTest.loadTradeById(2));
        assertEquals("Invalid Trade Id:" + 2, exception.getMessage());
    }
}
