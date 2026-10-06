package com.example.ecommerce.day_7;

import com.example.ecommerce.dto.request.UserRequest;
import com.example.ecommerce.dto.response.UserResponse;
import com.example.ecommerce.entity.UserEntity;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange
        UserRequest request = new UserRequest();
        request.setName("Minh");
        request.setEmail("minh@gmail.com");
        request.setPassword("123456");

        UserEntity savedUser =
                new UserEntity("Minh", "minh@gmail.com", "123456");
        savedUser.setId(1L);

        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(savedUser);

        // Act
        UserResponse response = userService.createUser(request);

        // Assert
        assertEquals(1L, response.getId());
        assertEquals("Minh", response.getName());
        assertEquals("minh@gmail.com", response.getEmail());

        // Verify
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void shouldReturnAllUsers() {
        // Arrange
        UserEntity user1 =
                new UserEntity("Minh", "minh@gmail.com", "123456");
        user1.setId(1L);

        UserEntity user2 =
                new UserEntity("An", "an@gmail.com", "123456");
        user2.setId(2L);

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        // Act
        List<UserResponse> response = userService.getAllUsers();

        // Assert
        assertEquals(2, response.size());

        assertEquals(1L, response.get(0).getId());
        assertEquals("Minh", response.get(0).getName());
        assertEquals("minh@gmail.com", response.get(0).getEmail());

        assertEquals(2L, response.get(1).getId());
        assertEquals("An", response.get(1).getName());
        assertEquals("an@gmail.com", response.get(1).getEmail());

        // Verify
        verify(userRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoUsers() {
        // Arrange
        when(userRepository.findAll())
                .thenReturn(List.of());

        // Act
        List<UserResponse> response = userService.getAllUsers();

        // Assert
        assertTrue(response.isEmpty());

        // Verify
        verify(userRepository).findAll();
    }

    @Test
    void shouldReturnUserById() {
        // Arrange
        UserEntity user =
                new UserEntity("Minh", "minh@gmail.com", "123456");
        user.setId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        // Act
        UserResponse response = userService.getUserById(1L);

        // Assert
        assertEquals(1L, response.getId());
        assertEquals("Minh", response.getName());
        assertEquals("minh@gmail.com", response.getEmail());

        // Verify
        verify(userRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserById(1L)
        );

        // Assert
        assertEquals("USER_NOT_FOUND", exception.getCode());

        // Verify
        verify(userRepository).findById(1L);
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        // Arrange
        UserEntity user =
                new UserEntity("Minh", "old@gmail.com", "123456");
        user.setId(1L);

        UserRequest request = new UserRequest();
        request.setName("Minh Nguyen");
        request.setEmail("minh@gmail.com");
        request.setPassword("abcdef");

        UserEntity updatedUser =
                new UserEntity("Minh Nguyen", "minh@gmail.com", "abcdef");
        updatedUser.setId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userRepository.save(user))
                .thenReturn(updatedUser);

        // Act
        UserResponse response =
                userService.updateUser(1L, request);

        // Assert
        assertEquals(1L, response.getId());
        assertEquals("Minh Nguyen", response.getName());
        assertEquals("minh@gmail.com", response.getEmail());

        assertEquals("Minh Nguyen", user.getName());
        assertEquals("minh@gmail.com", user.getEmail());
        assertEquals("abcdef", user.getPassword());

        // Verify
        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnUpdate() {
        // Arrange
        UserRequest request = new UserRequest();

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.updateUser(1L, request)
        );

        // Assert
        assertEquals("USER_NOT_FOUND", exception.getCode());

        // Verify
        verify(userRepository).findById(1L);
        verify(userRepository, never())
                .save(any(UserEntity.class));
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        // Arrange
        when(userRepository.existsById(1L))
                .thenReturn(true);

        // Act
        userService.deleteUser(1L);

        // Verify
        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnDelete() {
        // Arrange
        when(userRepository.existsById(1L))
                .thenReturn(false);

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.deleteUser(1L)
        );

        // Assert
        assertEquals("USER_NOT_FOUND", exception.getCode());

        // Verify
        verify(userRepository).existsById(1L);
        verify(userRepository, never())
                .deleteById(1L);
    }
}
