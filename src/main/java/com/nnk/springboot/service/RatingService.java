package com.nnk.springboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class RatingService {

    @Autowired
    RatingRepository ratingRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RatingDto mapToRatingDTO(Rating rating) {
        return new RatingDto(
                rating.getId(),rating.getMoodysRating(),rating.getSandPRating(),rating.getFitchRating(),rating.getOrderNumber());
    }

    public RatingDto loadRatingDtoById(Integer id) {
        Rating rating = ratingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Rating Id:" + id));
        return mapToRatingDTO(rating);
    }

    public List<RatingDto> loadRatingDtoList() {
        List<RatingDto> ratingDtoList = new ArrayList<>();
        List<Rating> ratings = ratingRepository.findAll();
        ratings.forEach(rating -> ratingDtoList.add(mapToRatingDTO(rating)));
        return ratingDtoList;
    }

    public void saveRating(RatingDto ratingDto) {
        Rating rating = objectMapper.convertValue(ratingDto, Rating.class);
        ratingRepository.save(rating);
    }

    public void updateRating(Rating ratingToUpdate, RatingDto ratingDto) {
        ratingToUpdate.setMoodysRating(ratingDto.getMoodysRating());
        ratingToUpdate.setSandPRating(ratingDto.getSandPRating());
        ratingToUpdate.setFitchRating(ratingDto.getFitchRating());
        ratingToUpdate.setOrderNumber(ratingDto.getOrderNumber());
        ratingRepository.save(ratingToUpdate);
    }

    public void deleteRating(Rating ratingToDelete) {
        ratingRepository.delete(ratingToDelete);
    }
}
