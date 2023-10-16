package com.nnk.springboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.CreateUserDto;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserDto mapToUserDTO(User user) {
        return new UserDto(
                user.getId(),user.getFullname(),user.getUsername(),user.getRole());
    }

    public CreateUserDto mapToCreateUserDTO(User user) {
        return new CreateUserDto(user.getId(),user.getFullname(),user.getPassword(),user.getUsername(), user.getRole());
    }

    public CreateUserDto loadUserDtoById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + id));
        return mapToCreateUserDTO(user);
    }

    public List<UserDto> loadUserDtoList() {
        List<UserDto> userDtoList = new ArrayList<>();
        List<User> users = userRepository.findAll();
        users.forEach(user -> userDtoList.add(mapToUserDTO(user)));
        return userDtoList;
    }

    public void saveUser(CreateUserDto createUserDto) {
        User user = objectMapper.convertValue(createUserDto, User.class);
        userRepository.save(user);
    }

    public void updateUser(User userToUpdate, CreateUserDto userDto) {
        userToUpdate = objectMapper.convertValue(userDto, User.class);
        userRepository.save(userToUpdate);
    }

    public void deleteUser(User userToDelete) {
        userRepository.delete(userToDelete);
    }
}
