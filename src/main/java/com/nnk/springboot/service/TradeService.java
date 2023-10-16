package com.nnk.springboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class TradeService {

    @Autowired
    TradeRepository tradeRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public TradeDto mapToTradeDTO(Trade trade) {
        return new TradeDto(trade.getId(),trade.getAccount(),trade.getType(),trade.getBuyQuantity());
    }

    public TradeDto loadTradeDtoById(Integer id) {
        Trade trade = tradeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Trade Id:" + id));
        return mapToTradeDTO(trade);
    }

    public List<TradeDto> loadTradeDtoList() {
        List<TradeDto> tradeDtoList = new ArrayList<>();
        List<Trade> trades = tradeRepository.findAll();
        trades.forEach(trade -> tradeDtoList.add(mapToTradeDTO(trade)));
        return tradeDtoList;
    }

    public void saveTrade(TradeDto tradeDto) {
        Trade trade = objectMapper.convertValue(tradeDto, Trade.class);
        trade.setTradeDate(Timestamp.from(Instant.now()));
        tradeRepository.save(trade);
    }

    public void updateTrade(Trade tradeToUpdate, TradeDto tradeDto) {
        tradeToUpdate.setId(tradeDto.getId());
        tradeToUpdate.setAccount(tradeDto.getAccount());
        tradeToUpdate.setType(tradeDto.getType());
        tradeToUpdate.setBuyQuantity(tradeDto.getBuyQuantity());
        tradeToUpdate.setRevisionDate(Timestamp.from(Instant.now()));
        tradeRepository.save(tradeToUpdate);
    }

    public void deleteTrade(Trade tradeToDelete) {
        tradeRepository.delete(tradeToDelete);
    }
}
