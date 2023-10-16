package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.CreateUserDto;
import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @RequestMapping("/user/list")
    public String home(Model model)
    {
        model.addAttribute("users", userService.loadUserDtoList());
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser(Model model) {
        model.addAttribute("user", new CreateUserDto());
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validate(@Valid @ModelAttribute("user") CreateUserDto userDto, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            userDto.setPassword(encoder.encode(userDto.getPassword()));
            userService.saveUser(userDto);
            model.addAttribute("users", userService.loadUserDtoList());
            return "redirect:/user/list";
        }
        return "user/add";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CreateUserDto userDto = userService.loadUserDtoById(id);
        model.addAttribute("user", userDto);
        return "user/update";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid @ModelAttribute("user") CreateUserDto userDto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }
        User userToUpdate = userRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("Invalid User Id:" + id));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        userService.updateUser(userToUpdate, userDto);
        model.addAttribute("users", userService.loadUserDtoList());
        return "redirect:/user/list";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        User userToDelete = userRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("Invalid User Id:" + id));
        userService.deleteUser(userToDelete);
        model.addAttribute("users", userService.loadUserDtoList());
        return "redirect:/user/list";
    }
}
