package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.service.RuleNameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RuleNameController {
    @Autowired
    private RuleNameService ruleNameService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping("/ruleName/list")
    public String home(Model model)
    {
        model.addAttribute("ruleNames", RuleNameDto.mapFromRuleNames(ruleNameService.loadRuleNameList()));
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleNameForm(Model model) {
        model.addAttribute("ruleName", new RuleNameDto());
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid @ModelAttribute("ruleName") RuleNameDto ruleNameDto, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            RuleName ruleNameToCreate = objectMapper.convertValue(ruleNameDto, RuleName.class);
            ruleNameService.createRuleName(ruleNameToCreate);
            model.addAttribute("ruleNames", RuleNameDto.mapFromRuleNames(ruleNameService.loadRuleNameList()));
            return "redirect:/ruleName/list";
        }
        return "ruleName/add";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("ruleName", RuleNameDto.mapFromRuleName(ruleNameService.loadRuleNameById(id)));
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @ModelAttribute("ruleName") @Valid RuleNameDto ruleName,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "ruleName/update";
        }
        ruleNameService.loadRuleNameById(id); //to check if the ruleName exists
        RuleName ruleNameToUpdate = objectMapper.convertValue(ruleName, RuleName.class);
        ruleNameService.updateRuleName(ruleNameToUpdate);
        model.addAttribute("ruleNames", RuleNameDto.mapFromRuleNames(ruleNameService.loadRuleNameList()));
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        ruleNameService.deleteRuleName(ruleNameService.loadRuleNameById(id));
        model.addAttribute("ruleNames", RuleNameDto.mapFromRuleNames(ruleNameService.loadRuleNameList()));
        return "redirect:/ruleName/list";
    }
}
