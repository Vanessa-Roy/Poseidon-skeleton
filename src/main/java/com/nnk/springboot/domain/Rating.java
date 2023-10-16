package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "rating")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @NotNull(message = "Moodys Rating is mandatory")
    private String moodysRating;
    @NotNull(message = "Sand Rating is mandatory")
    private String sandPRating;
    @NotNull(message = "Fitch Rating is mandatory")
    private String fitchRating;
    @NotNull(message = "Order Number is mandatory")
    private Integer orderNumber;


}
