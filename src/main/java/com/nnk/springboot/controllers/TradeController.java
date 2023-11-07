package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.security.AuthenticatedUserProvider;
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

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    AuthenticatedUserProvider authenticatedUserProvider;

    @RequestMapping("/trade/list")
    public String home(Model model)
    {
        model.addAttribute("trades", TradeDto.mapFromTrades(tradeService.loadTradeList()));
        model.addAttribute("isAdmin", authenticatedUserProvider.isAdmin(authenticatedUserProvider.getAuthenticatedUser()));
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
            Trade tradeToCreate = objectMapper.convertValue(tradeDto, Trade.class);
            tradeService.createTrade(tradeToCreate);
            model.addAttribute("trades", TradeDto.mapFromTrades(tradeService.loadTradeList()));
            return "redirect:/trade/list";
        }
        return "trade/add";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("trade", TradeDto.mapFromTrade(tradeService.loadTradeById(id)));
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid @ModelAttribute("trade") TradeDto tradeDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "trade/update";
        }
        tradeService.loadTradeById(id); //to check if the trade exists
        Trade tradeToUpdate = objectMapper.convertValue(tradeDto, Trade.class);
        tradeService.updateTrade(tradeToUpdate);
        model.addAttribute("trade", TradeDto.mapFromTrade(tradeService.loadTradeById(id)));
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        tradeService.deleteTrade(tradeService.loadTradeById(id));
        model.addAttribute("trades", TradeDto.mapFromTrades(tradeService.loadTradeList()));
        return "redirect:/trade/list";
    }
}
