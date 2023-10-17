package com.nnk.springboot.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.RuleName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RuleNameDto {
    private Integer id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Description is mandatory")
    private String description;
    @NotBlank(message = "Json is mandatory")
    private String json;
    @NotBlank(message = "Template is mandatory")
    private String template;
    @NotBlank(message = "Sql Str is mandatory")
    private String sqlStr;
    @NotBlank(message = "Sql Part is mandatory")
    private String sqlPart;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static RuleNameDto mapFromRuleName(RuleName ruleName) {
        return objectMapper.convertValue(ruleName, RuleNameDto.class);
    }

    public static List<RuleNameDto> mapFromRuleNames(List<RuleName> ruleNames) {
        return ruleNames.stream().map(RuleNameDto::mapFromRuleName).toList();
    }

}

