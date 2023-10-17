package com.nnk.springboot.service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleNameService {

    @Autowired
    RuleNameRepository ruleNameRepository;

    public RuleName loadRuleNameById(Integer id) {
        return ruleNameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid RuleName Id:" + id));
    }

    public List<RuleName> loadRuleNameList() {
        return ruleNameRepository.findAll();
    }

    public void createRuleName(RuleName ruleName) {
        ruleNameRepository.save(ruleName);
    }

    public void updateRuleName(RuleName ruleNameToUpdate) {
        ruleNameRepository.save(ruleNameToUpdate);
    }

    public void deleteRuleName(RuleName ruleNameToDelete) {
        ruleNameRepository.delete(ruleNameToDelete);
    }
}
