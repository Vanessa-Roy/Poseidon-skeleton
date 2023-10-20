package com.nnk.springboot.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.Rating;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    @Min(value = 0, message = "it must be positive")
    private Integer orderNumber;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static RatingDto mapFromRating(Rating rating) {
        return objectMapper.convertValue(rating, RatingDto.class);
    }

    public static List<RatingDto> mapFromRatings(List<Rating> ratings) {
        return ratings.stream().map(RatingDto::mapFromRating).toList();
    }

}

