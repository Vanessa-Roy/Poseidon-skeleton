package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.service.TradeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class TradeController {
    @Autowired
    private TradeService tradeService;

    @Autowired
    private TradeRepository tradeRepository;

    @RequestMapping("/trade/list")
    public String home(Model model)
    {
        model.addAttribute("trades", tradeService.loadTradeDtoList());
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addTradeForm(Model model) {
        model.addAttribute("trade", new TradeDto());
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid @ModelAttribute("trade") TradeDto tradeDto, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            tradeService.saveTrade(tradeDto);
            model.addAttribute("trades", tradeService.loadTradeDtoList());
            return "redirect:/trade/list";
        }
        return "trade/add";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        TradeDto tradeDto = tradeService.loadTradeDtoById(id);
        model.addAttribute("trade", tradeDto);
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid TradeDto trade, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "trade/update";
        }
        Trade tradeToUpdate = tradeRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("Invalid Curve Point Id:" + id));
        tradeService.updateTrade(tradeToUpdate, trade);
        model.addAttribute("trades", tradeService.loadTradeDtoList());
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        Trade tradeToDelete = tradeRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("Invalid Curve Point Id:" + id));
        tradeService.deleteTrade(tradeToDelete);
        model.addAttribute("trades", tradeService.loadTradeDtoList());
        return "redirect:/trade/list";
    }
}
