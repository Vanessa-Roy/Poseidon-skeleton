package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.repositories.RatingRepository;
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

    @Autowired
    private RatingRepository ratingRepository;

    @RequestMapping("/rating/list")
    public String home(Model model)
    {
        model.addAttribute("ratings", ratingService.loadRatingDtoList());
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
            ratingService.saveRating(ratingDto);
            model.addAttribute("ratings", ratingService.loadRatingDtoList());
            return "redirect:/rating/list";
        }
        return "rating/add";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        RatingDto ratingDto = ratingService.loadRatingDtoById(id);
        model.addAttribute("rating", ratingDto);
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid RatingDto rating,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "rating/update";
        }
        Rating ratingToUpdate = ratingRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("Invalid Rating Id:" + id));
        ratingService.updateRating(ratingToUpdate, rating);
        model.addAttribute("ratings", ratingService.loadRatingDtoList());
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        Rating ratingToDelete = ratingRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("Invalid Rating Id:" + id));
        ratingService.deleteRating(ratingToDelete);
        model.addAttribute("ratings", ratingService.loadRatingDtoList());
        return "redirect:/rating/list";
    }
}
