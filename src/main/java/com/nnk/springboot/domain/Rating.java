package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rating")
@Setter
@Getter
@NoArgsConstructor
public class Rating {
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private String moodysRating;
    @Column(nullable = false)
    private String sandPRating;
    @Column(nullable = false)
    private String fitchRating;
    @Column(nullable = false)
    private Integer orderNumber;


}
