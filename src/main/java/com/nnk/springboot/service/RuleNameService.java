package com.nnk.springboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuleNameService {

    @Autowired
    RuleNameRepository ruleNameRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RuleNameDto mapToRuleNameDTO(RuleName ruleName) {
        return new RuleNameDto(
                ruleName.getId(),
                ruleName.getName(),
                ruleName.getDescription(),
                ruleName.getJson(),
                ruleName.getTemplate(),
                ruleName.getSqlStr(),
                ruleName.getSqlPart());
    }

    public RuleNameDto loadRuleNameDtoById(Integer id) {
        RuleName ruleName = ruleNameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid RuleName Id:" + id));
        return mapToRuleNameDTO(ruleName);
    }

    public List<RuleNameDto> loadRuleNameDtoList() {
        List<RuleNameDto> ruleNameDtoList = new ArrayList<>();
        List<RuleName> ruleNames = ruleNameRepository.findAll();
        ruleNames.forEach(ruleName -> ruleNameDtoList.add(mapToRuleNameDTO(ruleName)));
        return ruleNameDtoList;
    }

    public void saveRuleName(RuleNameDto ruleNameDto) {
        RuleName ruleName = objectMapper.convertValue(ruleNameDto, RuleName.class);
        ruleNameRepository.save(ruleName);
    }

    public void updateRuleName(RuleName ruleNameToUpdate, RuleNameDto ruleNameDto) {
        ruleNameToUpdate = objectMapper.convertValue(ruleNameDto, RuleName.class);
        ruleNameRepository.save(ruleNameToUpdate);
    }

    public void deleteRuleName(RuleName ruleNameToDelete) {
        ruleNameRepository.delete(ruleNameToDelete);
    }
}
