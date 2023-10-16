package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.repositories.BidListRepository;
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

    @Autowired
    private BidListRepository bidListRepository;

    @RequestMapping("/bidList/list")
    public String home(Model model)
    {
        model.addAttribute("bidLists", bidListService.loadBidListDtoList());
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
            bidListService.saveBidList(bidListDto);
            model.addAttribute("bidLists", bidListService.loadBidListDtoList());
            return "redirect:/bidList/list";
        }
        return "bidList/add";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidListDto bidListDto = bidListService.loadBidListDtoById(id);
        model.addAttribute("bidList", bidListDto);
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBidList(@PathVariable("id") Integer id, @Valid BidListDto bidList, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/update";
        }
        BidList bidListToUpdate = bidListRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("Invalid Curve Point Id:" + id));
        bidListService.updateBidList(bidListToUpdate, bidList);
        model.addAttribute("bidLists", bidListService.loadBidListDtoList());
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBidList(@PathVariable("id") Integer id, Model model) {
        BidList bidListToDelete = bidListRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("Invalid Curve Point Id:" + id));
        bidListService.deleteBidList(bidListToDelete);
        model.addAttribute("bidLists", bidListService.loadBidListDtoList());
        return "redirect:/bidList/list";
    }
}
