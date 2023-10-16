package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank (message = "must not be null")
    private String moodysRating;
    @NotBlank(message = "must not be null")
    private String sandPRating;
    @NotBlank (message = "must not be null")
    private String fitchRating;
    @NotNull (message = "must not be null")
    private Integer orderNumber;

}

