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
    @NotNull (message = "Curve Id is mandatory")
    private Integer curveId;
    @NotNull (message = "Term is mandatory")
    private Double term;
    @NotNull (message = "Value is mandatory")
    private Double value;

}

