package com.nnk.springboot.service;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class TradeService {

    @Autowired
    TradeRepository tradeRepository;

    public Trade loadTradeById(Integer id) {
        return tradeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Trade Id:" + id));
    }

    public List<Trade> loadTradeList() {
        return tradeRepository.findAll();
    }

    public void createTrade(Trade trade) {
        tradeRepository.save(trade);
    }

    public void updateTrade(Trade tradeToUpdate) {
        tradeRepository.save(tradeToUpdate);
    }

    public void deleteTrade(Trade tradeToDelete) {
        tradeRepository.delete(tradeToDelete);
    }
}
