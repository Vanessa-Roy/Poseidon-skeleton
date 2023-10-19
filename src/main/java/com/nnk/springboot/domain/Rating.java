package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @NotBlank
    private String moodysRating;
    @NotBlank
    private String sandPRating;
    @NotBlank
    private String fitchRating;
    @NotNull
    private Integer orderNumber;


}
