package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurvePointDto {
    private Integer id;
    @NotNull (message = "must not be null")
    private Integer curveId;
    @NotNull (message = "must not be null")
    private Double term;
    @NotNull (message = "must not be null")
    private Double value;

}

