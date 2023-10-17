package com.nnk.springboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.Rating;
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

    public Rating loadRatingById(Integer id) {
        return ratingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Rating Id:" + id));
    }

    public List<Rating> loadRatingList() {
        return ratingRepository.findAll();
    }

    public void createRating(Rating rating) {
        ratingRepository.save(rating);
    }

    public void updateRating(Rating ratingToUpdate) {
        ratingRepository.save(ratingToUpdate);
    }

    public void deleteRating(Rating ratingToDelete) {
        ratingRepository.delete(ratingToDelete);
    }
}
