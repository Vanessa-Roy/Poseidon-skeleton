package com.nnk.springboot.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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
    @Column(nullable = false)
    private Integer curveId;
    private Timestamp asOfDate;
    @Column(nullable = false)
    private Double term;
    @Column(nullable = false)
    private Double value;
    @CreationTimestamp
    @Column(updatable=false)
    private Timestamp creationDate;

}
