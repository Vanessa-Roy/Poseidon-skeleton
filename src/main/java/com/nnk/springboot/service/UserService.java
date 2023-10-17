package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User loadUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + id));
    }

    public List<User> loadUserList() {
        return userRepository.findAll();
    }

    public void createUser(User user) {

        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void updateUser(User userToUpdate) {
        userToUpdate.setPassword(encoder.encode(userToUpdate.getPassword()));
        userRepository.save(userToUpdate);
    }

    public void deleteUser(User userToDelete) {
        userRepository.delete(userToDelete);
    }
}
