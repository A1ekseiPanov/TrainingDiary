package ru.panov.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.panov.config.TestConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.panov.util.PathConstants.*;

@SpringBootTest(classes = {TestConfig.class})
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Успешный вход")
    void login_ValidRequest() throws Exception {
        mockMvc.perform(post(AUTH_PATH + LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "user1",
                                "password":"user1"
                                }
                                """))
                .andExpectAll(status().isOk());
    }

    @Test
    @DisplayName("Вход с неверными данными")
    void login_InvalidLoginInformation_ReturnsConflict() throws Exception {
        mockMvc.perform(post(AUTH_PATH + LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "user",
                                "password":"user"
                                }
                                """))
                .andExpectAll(status().isConflict(),
                        content().json("""
                                {
                                "detail": "неправильное имя пользователя или пароль"
                                }
                                """));
    }

    @Test
    @DisplayName("Успешная регистрация")
    void registration_ValidRequest_ReturnsUserResponse() throws Exception {
        mockMvc.perform(post(AUTH_PATH + REGISTRATION_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "username",
                                "password":"password"
                                }
                                """))
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
    @DisplayName("Регистрация пользователя, который уже существует")
    void registration_UserIsPresent_ReturnsConflict() throws Exception {
        mockMvc.perform(post(AUTH_PATH + REGISTRATION_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "user1",
                                "password":"user1"
                                }
                                """))
                .andExpectAll(status().isConflict(),
                        content().json("""
                                {
                                "detail": "Такой пользователь уже существует"
                                }
                                """));
    }

    @Test
    @DisplayName("Регистрация с пустыми данными")
    void registration_UsernameAndPasswordIsBlank_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post(AUTH_PATH + REGISTRATION_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                }"""))
                .andExpectAll(status().isBadRequest(),
                        content().json("""
                                {
                                "errors": [
                                      "password не может быть пустыми или состоять только из пробелов.",
                                      "username не может быть пустыми или состоять только из пробелов."
                                    ]
                                }"""));
    }

    @Test
    @DisplayName("Регистрация с коротким паролем")
    void registration_PasswordSizeSmall_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post(AUTH_PATH + REGISTRATION_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "user",
                                "password":"u"
                                }
                                """))
                .andExpectAll(status().isBadRequest(),
                        content().json("""
                                {
                                "errors": [
                                      "Длина password должна составлять от 5 до 30 символов."
                                    ]
                                }"""));
    }
}