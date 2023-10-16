package com.nnk.springboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class BidListService {

    @Autowired
    BidListRepository bidListRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public BidListDto mapToBidListDTO(BidList bidList) {
        return new BidListDto(bidList.getId(),bidList.getAccount(),bidList.getType(),bidList.getBidQuantity());
    }

    public BidListDto loadBidListDtoById(Integer id) {
        BidList bidList = bidListRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid BidList Id:" + id));
        return mapToBidListDTO(bidList);
    }

    public List<BidListDto> loadBidListDtoList() {
        List<BidListDto> bidListDtoList = new ArrayList<>();
        List<BidList> bidLists = bidListRepository.findAll();
        bidLists.forEach(bidList -> bidListDtoList.add(mapToBidListDTO(bidList)));
        return bidListDtoList;
    }

    public void saveBidList(BidListDto bidListDto) {
        BidList bidList = objectMapper.convertValue(bidListDto, BidList.class);
        bidList.setCreationDate(Timestamp.from(Instant.now()));
        bidListRepository.save(bidList);
    }

    public void updateBidList(BidList bidListToUpdate, BidListDto bidListDto) {
        bidListToUpdate.setId(bidListDto.getId());
        bidListToUpdate.setAccount(bidListDto.getAccount());
        bidListToUpdate.setType(bidListDto.getType());
        bidListToUpdate.setBidQuantity(bidListDto.getBidQuantity());
        bidListToUpdate.setRevisionDate(Timestamp.from(Instant.now()));
        bidListRepository.save(bidListToUpdate);
    }

    public void deleteBidList(BidList bidListToDelete) {
        bidListRepository.delete(bidListToDelete);
    }
}
