package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public User loadUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + id));
    }

    public List<User> loadUserList() {
        return userRepository.findAll();
    }

    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void createUser(User user) {
        User existingUser = loadUserByUsername(user.getUsername());

        if(existingUser != null){
            throw new IllegalArgumentException("User already existing: " + user.getUsername());
        }

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
