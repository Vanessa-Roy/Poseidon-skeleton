package com.nnk.springboot.dto;

import com.nnk.springboot.domain.CurvePoint;
import jakarta.validation.constraints.Min;
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
    @Min(value = 0, message = "it must be positive")
    private Integer curveId;
    @NotNull (message = "Term is mandatory")
    @Min(value = 0, message = "it must be positive")
    private Double term;
    @NotNull (message = "Value is mandatory")
    @Min(value = 0, message = "it must be positive")
    private Double value;

    public static CurvePointDto mapFromCurvePoint(CurvePoint curvePoint) {
        return new CurvePointDto(curvePoint.getId(),curvePoint.getCurveId(),curvePoint.getTerm(),curvePoint.getValue());
    }

    public static List<CurvePointDto> mapFromCurvePoints(List<CurvePoint> curvePoints) {
        return curvePoints.stream().map(CurvePointDto::mapFromCurvePoint).toList();
    }

}

