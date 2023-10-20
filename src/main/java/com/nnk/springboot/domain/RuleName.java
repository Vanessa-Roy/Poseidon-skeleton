package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rulename")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RuleName {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String json;
    @Column(nullable = false)
    private String template;
    @Column(nullable = false)
    private String sqlStr;
    @Column(nullable = false)
    private String sqlPart;
}
