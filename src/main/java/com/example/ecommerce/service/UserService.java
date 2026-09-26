package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.UserRequest;
import com.example.ecommerce.dto.response.UserResponse;
import com.example.ecommerce.entity.UserEntity;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(UserRequest request) {

        UserEntity user = new UserEntity(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );

        UserEntity savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }

    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                ))
                .toList();
    }

    public UserResponse getUserById(Long id) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public UserResponse updateUser(Long id, UserRequest request) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        UserEntity updatedUser = userRepository.save(user);

        return new UserResponse(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail()
        );
    }

    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(id);
    }
}