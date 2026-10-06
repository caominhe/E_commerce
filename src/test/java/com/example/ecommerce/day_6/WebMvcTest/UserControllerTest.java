package com.example.ecommerce.day_6.WebMvcTest;

import com.example.ecommerce.controller.UserController;
import com.example.ecommerce.dto.request.UserRequest;
import com.example.ecommerce.dto.response.UserResponse;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    // 1. POST /api/users - Create success
    @Test
    void shouldCreateUserSuccessfully() throws Exception {

        UserResponse response =
                new UserResponse(1L, "Minh", "minh@gmail.com");

        when(userService.createUser(any(UserRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Minh",
                                    "email": "minh@gmail.com",
                                    "password": "123456"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Minh"))
                .andExpect(jsonPath("$.email").value("minh@gmail.com"));

        verify(userService).createUser(any(UserRequest.class));
    }

    // 2. POST /api/users - Name invalid
    @Test
    void shouldReturnBadRequestWhenNameIsInvalid() throws Exception {

        mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "",
                                    "email": "minh@gmail.com",
                                    "password": "123456"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value(
                        org.hamcrest.Matchers.containsString("name")))
                .andExpect(jsonPath("$.path").value("/api/users"));

        verify(userService, never())
                .createUser(any(UserRequest.class));
    }

    // 3. POST /api/users - Email invalid
    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {

        mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Minh",
                                    "email": "invalid-email",
                                    "password": "123456"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value(
                        org.hamcrest.Matchers.containsString("email")))
                .andExpect(jsonPath("$.path").value("/api/users"));

        verify(userService, never())
                .createUser(any(UserRequest.class));
    }

    // 4. POST /api/users - Password invalid
    @Test
    void shouldReturnBadRequestWhenPasswordIsInvalid() throws Exception {

        mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Minh",
                                    "email": "minh@gmail.com",
                                    "password": "123"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value(
                        org.hamcrest.Matchers.containsString("password")))
                .andExpect(jsonPath("$.path").value("/api/users"));

        verify(userService, never())
                .createUser(any(UserRequest.class));
    }

    // 5. GET /api/users - Get all
    @Test
    void shouldReturnAllUsers() throws Exception {

        List<UserResponse> users = List.of(
                new UserResponse(1L, "Minh", "minh@gmail.com"),
                new UserResponse(2L, "An", "an@gmail.com")
        );

        when(userService.getAllUsers())
                .thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))

                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Minh"))
                .andExpect(jsonPath("$[0].email").value("minh@gmail.com"))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("An"))
                .andExpect(jsonPath("$[1].email").value("an@gmail.com"));

        verify(userService).getAllUsers();
    }

    // 6. GET /api/users/{id} - Get success
    @Test
    void shouldReturnUserById() throws Exception {

        UserResponse response =
                new UserResponse(1L, "Minh", "minh@gmail.com");

        when(userService.getUserById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Minh"))
                .andExpect(jsonPath("$.email").value("minh@gmail.com"));

        verify(userService).getUserById(1L);
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {

        when(userService.getUserById(99L))
                .thenThrow(new ResourceNotFoundException(
                        "USER_NOT_FOUND",
                        "User not found"
                ));

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("User not found"))
                .andExpect(jsonPath("$.path").value("/api/users/99"));

        verify(userService).getUserById(99L);
    }

    // 7. PUT /api/users/{id} - Update success
    @Test
    void shouldUpdateUserSuccessfully() throws Exception {

        UserResponse response =
                new UserResponse(
                        1L,
                        "Minh Nguyen",
                        "minh@gmail.com"
                );

        when(userService.updateUser(
                eq(1L),
                any(UserRequest.class)
        )).thenReturn(response);

        mockMvc.perform(put("/api/users/1")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Minh Nguyen",
                                    "email": "minh@gmail.com",
                                    "password": "abcdef"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Minh Nguyen"))
                .andExpect(jsonPath("$.email").value("minh@gmail.com"));

        verify(userService).updateUser(
                eq(1L),
                any(UserRequest.class)
        );
    }

    // 8. PUT /api/users/{id} - Validation fail
    @Test
    void shouldReturnBadRequestWhenUpdateRequestIsInvalid()
            throws Exception {

        mockMvc.perform(put("/api/users/1")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "M",
                                    "email": "invalid",
                                    "password": "123"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.path").value("/api/users/1"));

        verify(userService, never())
                .updateUser(
                        eq(1L),
                        any(UserRequest.class)
                );
    }

    // 9. DELETE /api/users/{id} - Delete success
    @Test
    void shouldDeleteUserSuccessfully() throws Exception {

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        verify(userService).deleteUser(1L);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingUser()
            throws Exception {

        doThrow(new ResourceNotFoundException(
                "USER_NOT_FOUND",
                "User not found"
        ))
                .when(userService)
                .deleteUser(99L);

        mockMvc.perform(delete("/api/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("User not found"))
                .andExpect(jsonPath("$.path").value("/api/users/99"));

        verify(userService).deleteUser(99L);
    }
}

