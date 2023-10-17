package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.SaveUserDto;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping("/user/list")
    public String home(Model model)
    {
        model.addAttribute("users", UserDto.mapFromUsers(userService.loadUserList()));
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser(Model model) {
        model.addAttribute("user", new SaveUserDto());
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validate(@Valid @ModelAttribute("user") SaveUserDto userDto, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            User userToCreate = objectMapper.convertValue(userDto, User.class);
            userService.createUser(userToCreate);
            model.addAttribute("users", UserDto.mapFromUsers(userService.loadUserList()));
            return "redirect:/user/list";
        }
        return "user/add";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("user", SaveUserDto.mapFromUser(userService.loadUserById(id)));
        return "user/update";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid @ModelAttribute("user") SaveUserDto userDto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }
        userService.loadUserById(id); //to check if the user exists
        User userToUpdate = objectMapper.convertValue(userDto, User.class);
        userService.updateUser(userToUpdate);
        model.addAttribute("users", UserDto.mapFromUsers(userService.loadUserList()));
        return "redirect:/user/list";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        userService.deleteUser(userService.loadUserById(id));
        model.addAttribute("users", UserDto.mapFromUsers(userService.loadUserList()));
        return "redirect:/user/list";
    }
}
