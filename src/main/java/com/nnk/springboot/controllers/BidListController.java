package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.service.BidListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class BidListController {
    @Autowired
    private BidListService bidListService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping("/bidList/list")
    public String home(Model model)
    {
        model.addAttribute("bidLists", BidListDto.mapFromBidLists(bidListService.loadBidListList()));
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidListForm(Model model) {
        model.addAttribute("bidList", new BidListDto());
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid @ModelAttribute("bidList") BidListDto bidListDto, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            BidList bidList = objectMapper.convertValue(bidListDto, BidList.class);
            bidListService.createBidList(bidList);
            model.addAttribute("bidLists", BidListDto.mapFromBidLists(bidListService.loadBidListList()));
            return "redirect:/bidList/list";
        }
        return "bidList/add";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("bidList", BidListDto.mapFromBidList(bidListService.loadBidListById(id)));
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBidList(@PathVariable("id") Integer id, @ModelAttribute("bidList") @Valid BidListDto bidList, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/update";
        }
        bidListService.loadBidListById(id); //to check if the bidList exists
        BidList bidListToUpdate = objectMapper.convertValue(bidList, BidList.class);
        bidListService.updateBidList(bidListToUpdate);
        model.addAttribute("bidLists", BidListDto.mapFromBidLists(bidListService.loadBidListList()));
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBidList(@PathVariable("id") Integer id, Model model) {
        bidListService.deleteBidList(bidListService.loadBidListById(id));
        model.addAttribute("bidLists", BidListDto.mapFromBidLists(bidListService.loadBidListList()));
        return "redirect:/bidList/list";
    }
}
