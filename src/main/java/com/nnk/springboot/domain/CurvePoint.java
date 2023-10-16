package com.nnk.springboot.domain;


import com.fasterxml.jackson.annotation.JsonAnySetter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "curvepoint")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurvePoint {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @NotNull
    private Integer curveId;
    private Timestamp asOfDate;
    @NotNull
    private Double term;
    @NotNull
    private Double value;
    @NotNull(message = "Creation Date is mandatory")
    private Timestamp creationDate;

}
