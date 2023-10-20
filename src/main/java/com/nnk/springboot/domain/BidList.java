package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "bidlist")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BidList {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private String account;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private Double bidQuantity;
    private Double askQuantity;
    private Double bid;
    private Double ask;
    private String benchmark;
    private Timestamp bidListDate;
    private String commentary;
    private String security;
    private String status;
    private String trader;
    private String book;
    private String creationName;
    @CreationTimestamp
    @Column(updatable=false)
    private Timestamp creationDate;
    private String revisionName;
    @UpdateTimestamp
    private Timestamp revisionDate;
    private String dealName;
    private String dealType;
    private String sourceListId;
    private String side;
}
