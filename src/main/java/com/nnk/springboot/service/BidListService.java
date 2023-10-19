package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidListService {

    @Autowired
    BidListRepository bidListRepository;

    public BidList loadBidListById(Integer id) {
        return bidListRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid BidList Id:" + id));
    }

    public List<BidList> loadBidListList() {
        return bidListRepository.findAll();
    }

    public void createBidList(BidList bidList) {
        bidListRepository.save(bidList);
    }

    public void updateBidList(BidList bidListToUpdate) {
        bidListRepository.save(bidListToUpdate);
    }

    public void deleteBidList(BidList bidListToDelete) {
        bidListRepository.delete(bidListToDelete);
    }
}
