package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;
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

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping("/curvePoint/list")
    public String home(Model model)
    {
        model.addAttribute("curvePoints", CurvePointDto.mapFromCurvePoints(curvePointService.loadCurvePointList()));
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
            CurvePoint curvePointToCreate = objectMapper.convertValue(curvePointDto, CurvePoint.class);
            curvePointService.createCurvePoint(curvePointToCreate);
            model.addAttribute("curvePoints", CurvePointDto.mapFromCurvePoints(curvePointService.loadCurvePointList()));
            return "redirect:/curvePoint/list";
        }
        return "curvePoint/add";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("curvePoint", CurvePointDto.mapFromCurvePoint(curvePointService.loadCurvePointById(id)));
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @ModelAttribute("curvePoint") @Valid CurvePointDto curvePoint, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "curvePoint/update";
        }
        curvePointService.loadCurvePointById(id); //to check if the curvePoint exists
        CurvePoint curvePointToUpdate = objectMapper.convertValue(curvePoint, CurvePoint.class);
        curvePointService.updateCurvePoint(curvePointToUpdate);
        model.addAttribute("curvePoints", CurvePointDto.mapFromCurvePoints(curvePointService.loadCurvePointList()));
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
        curvePointService.deleteCurvePoint(curvePointService.loadCurvePointById(id));
        model.addAttribute("curvePoints", CurvePointDto.mapFromCurvePoints(curvePointService.loadCurvePointList()));
        return "redirect:/curvePoint/list";
    }
}
