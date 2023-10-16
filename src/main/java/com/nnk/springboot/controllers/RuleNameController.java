package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.repositories.RuleNameRepository;
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

    @Autowired
    private RuleNameRepository ruleNameRepository;

    @RequestMapping("/ruleName/list")
    public String home(Model model)
    {
        model.addAttribute("ruleNames", ruleNameService.loadRuleNameDtoList());
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
            ruleNameService.saveRuleName(ruleNameDto);
            model.addAttribute("ruleNames", ruleNameService.loadRuleNameDtoList());
            return "redirect:/ruleName/list";
        }
        return "ruleName/add";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        RuleNameDto ruleNameDto = ruleNameService.loadRuleNameDtoById(id);
        model.addAttribute("ruleName", ruleNameDto);
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @ModelAttribute("ruleName") @Valid RuleNameDto ruleName,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "ruleName/update";
        }
        RuleName ruleNameToUpdate = ruleNameRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("Invalid RuleName Id:" + id));
        ruleNameService.updateRuleName(ruleNameToUpdate, ruleName);
        model.addAttribute("ruleNames", ruleNameService.loadRuleNameDtoList());
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        RuleName ruleNameToDelete = ruleNameRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("Invalid RuleName Id:" + id));
        ruleNameService.deleteRuleName(ruleNameToDelete);
        model.addAttribute("ruleNames", ruleNameService.loadRuleNameDtoList());
        return "redirect:/ruleName/list";
    }
}
