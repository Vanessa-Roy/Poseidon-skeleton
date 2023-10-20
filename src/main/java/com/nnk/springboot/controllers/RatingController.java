package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.security.AuthenticatedUserProvider;
import com.nnk.springboot.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RatingController {
    @Autowired
    private RatingService ratingService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    AuthenticatedUserProvider authenticatedUserProvider;

    @RequestMapping("/rating/list")
    public String home(Model model)
    {
        model.addAttribute("ratings", RatingDto.mapFromRatings(ratingService.loadRatingList()));
        model.addAttribute("isAdmin", authenticatedUserProvider.isAdmin(authenticatedUserProvider.getAuthenticatedUser()));
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Model model) {
        model.addAttribute("rating", new RatingDto());
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid @ModelAttribute("rating") RatingDto ratingDto, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            Rating ratingToCreate = objectMapper.convertValue(ratingDto, Rating.class);
            ratingService.createRating(ratingToCreate);
            model.addAttribute("ratings", RatingDto.mapFromRatings(ratingService.loadRatingList()));
            return "redirect:/rating/list";
        }
        return "rating/add";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("rating", RatingDto.mapFromRating(ratingService.loadRatingById(id)));
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid @ModelAttribute("rating") RatingDto rating,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "rating/update";
        }
        ratingService.loadRatingById(id); //to check if the ruleName exists
        Rating ratingToUpdate = objectMapper.convertValue(rating, Rating.class);
        ratingService.updateRating(ratingToUpdate);
        model.addAttribute("ratings", RatingDto.mapFromRatings(ratingService.loadRatingList()));
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        ratingService.deleteRating(ratingService.loadRatingById(id));
        model.addAttribute("ratings", RatingDto.mapFromRatings(ratingService.loadRatingList()));
        return "redirect:/rating/list";
    }
}
