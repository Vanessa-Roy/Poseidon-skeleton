package com.nnk.springboot.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.CurvePoint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static CurvePointDto mapFromCurvePoint(CurvePoint curvePoint) {
        return new CurvePointDto(curvePoint.getId(),curvePoint.getCurveId(),curvePoint.getTerm(),curvePoint.getValue());
    }

    public static List<CurvePointDto> mapFromCurvePoints(List<CurvePoint> curvePoints) {
        return curvePoints.stream().map(CurvePointDto::mapFromCurvePoint).toList();
    }

}

