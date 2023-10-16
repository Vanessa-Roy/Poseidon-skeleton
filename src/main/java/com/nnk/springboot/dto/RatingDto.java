package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RatingDto {
    private Integer id;
    @NotNull (message = "must not be null")
    private String moodysRating;
    @NotNull (message = "must not be null")
    private String sandPRating;
    @NotNull (message = "must not be null")
    private String fitchRating;
    @NotNull (message = "must not be null")
    private Integer orderNumber;

}

