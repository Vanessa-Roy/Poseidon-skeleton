package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RuleNameDto {
    private Integer id;
    @NotBlank(message = "must not be null")
    private String name;
    @NotBlank(message = "must not be null")
    private String description;
    @NotBlank(message = "must not be null")
    private String json;
    @NotBlank(message = "must not be null")
    private String template;
    @NotBlank(message = "must not be null")
    private String sqlStr;
    @NotBlank(message = "must not be null")
    private String sqlPart;

}

