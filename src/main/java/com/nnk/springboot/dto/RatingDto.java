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
    @NotBlank (message = "Moodys Rating is mandatory")
    private String moodysRating;
    @NotBlank(message = "Sand Rating is mandatory")
    private String sandPRating;
    @NotBlank (message = "Fitch Rating is mandatory")
    private String fitchRating;
    @NotNull (message = "Order Number is mandatory")
    private Integer orderNumber;

}

