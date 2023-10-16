package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BidListDto {
    private Integer id;
    @NotBlank(message = "must not be null")
    private String account;
    @NotBlank(message = "must not be null")
    private String type;
    @NotNull(message = "must not be null")
    private Double bidQuantity;

}

