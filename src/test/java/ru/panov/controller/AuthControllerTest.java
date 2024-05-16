package ru.panov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.panov.config.TestConfig;
import ru.panov.model.dto.request.UserRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.panov.util.PathConstants.*;

@SpringBootTest(classes = {TestConfig.class})
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_ValidRequest() throws Exception {
        UserRequest userRequest = UserRequest.builder()
                .username("user1")
                .password("user1")
                .build();
        mockMvc.perform(post(AUTH_PATH + LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpectAll(status().isOk());
    }

    @Test
    void login_InvalidLoginInformation_ReturnsConflict() throws Exception {
        UserRequest userRequest = UserRequest.builder()
                .username("user")
                .password("user")
                .build();
        mockMvc.perform(post(AUTH_PATH + LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpectAll(status().isConflict(),
                        content().json("""
                                {
                                "detail": "неправильное имя пользователя или пароль"
                                }
                                """));
    }

    @Test
    void registration_ValidRequest_ReturnsUserResponse() throws Exception {
        UserRequest userRequest = UserRequest.builder()
                .username("username")
                .password("password")
                .build();

        mockMvc.perform(post(AUTH_PATH + REGISTRATION_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpectAll(status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "http://localhost/users/3"),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                "username": "username",
                                "role":"USER"
                                }
                                """));
    }

    @Test
    void registration_UserIsPresent_ReturnsConflict() throws Exception {
        UserRequest userRequest = UserRequest.builder()
                .username("user1")
                .password("user1")
                .build();

        mockMvc.perform(post(AUTH_PATH + REGISTRATION_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpectAll(status().isConflict(),
                        content().json("""
                                {
                                "detail": "Такой пользователь уже существует"
                                }
                                """));
    }

    @Test
    void registration_UsernameIsBlank_ReturnsBadRequest() throws Exception {
        UserRequest userRequest = UserRequest.builder()
                .username("")
                .password("user1")
                .build();

        mockMvc.perform(post(AUTH_PATH + REGISTRATION_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpectAll(status().isBadRequest(),
                        content().json("""
                                {
                                "detail": "Username и password не могут быть пустыми или состоять только из пробелов."
                                }
                                """));
    }
}