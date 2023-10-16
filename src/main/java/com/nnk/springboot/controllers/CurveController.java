package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.service.CurvePointService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class CurveController {
    @Autowired
    private CurvePointService curvePointService;

    @Autowired
    private CurvePointRepository curvePointRepository;

    @RequestMapping("/curvePoint/list")
    public String home(Model model)
    {
        model.addAttribute("curvePoints", curvePointService.loadCurvePointDtoList());
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(Model model) {
        model.addAttribute("curvePoint", new CurvePointDto());
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid @ModelAttribute("curvePoint") CurvePointDto curvePointDto, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            curvePointService.saveCurvePoint(curvePointDto);
            model.addAttribute("curvePoints", curvePointService.loadCurvePointDtoList());
            return "redirect:/curvePoint/list";
        }
        return "curvePoint/add";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePointDto curvePointDto = curvePointService.loadCurvePointDtoById(id);
        model.addAttribute("curvePoint", curvePointDto);
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @ModelAttribute("curvePoint") @Valid CurvePointDto curvePoint, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "curvePoint/update";
        }
        CurvePoint curvePointToUpdate = curvePointRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("Invalid Curve Point Id:" + id));
        curvePointService.updateCurvePoint(curvePointToUpdate, curvePoint);
        model.addAttribute("curvePoints", curvePointService.loadCurvePointDtoList());
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
        CurvePoint curvePointToDelete = curvePointRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("Invalid Curve Point Id:" + id));
        curvePointService.deleteCurvePoint(curvePointToDelete);
        model.addAttribute("curvePoints", curvePointService.loadCurvePointDtoList());
        return "redirect:/curvePoint/list";
    }
}
