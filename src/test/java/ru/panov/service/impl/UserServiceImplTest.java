package ru.panov.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.panov.dao.UserDAO;
import ru.panov.exception.InputDataConflictException;
import ru.panov.exception.ValidationException;
import ru.panov.model.User;
import ru.panov.model.dto.request.UserRequest;
import ru.panov.security.JwtService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserDAO userDAO;
    @Mock
    private JwtService jwtService;

    @Test
    @DisplayName("Регистрация, успешная регистрация пользователя")
    void register_ValidUserRegisterSuccess() {
        UserRequest userRequest = UserRequest.builder()
                .username("user1")
                .password("user1")
                .build();
        when(userDAO.findByUsername(userRequest.getUsername())).thenReturn(Optional.empty());

        userService.register(userRequest);

        verify(userDAO, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Регистрация, пользователь уже существует")
    void register_ExistingUserThrowsInputDataConflictException() {
        UserRequest userRequest = UserRequest.builder()
                .username("existingUser")
                .password("password")
                .build();
        when(userDAO.findByUsername(userRequest.getUsername())).thenReturn(Optional.of(User.builder().build()));

        assertThatThrownBy(() -> userService.register(userRequest))
                .isInstanceOf(InputDataConflictException.class);

        verify(userDAO, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Регистрация, нулевое имя пользователя вызывает исключение ValidationException")
    void register_NullUsernameThrowsValidationException() {
        UserRequest userRequest = UserRequest.builder()
                .username(null)
                .password("password")
                .build();

        assertThatThrownBy(() -> userService.register(userRequest))
                .isInstanceOf(ValidationException.class);

        verify(userDAO, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Регистрация, короткий пароль вызывает исключение ValidationException")
    void register_PasswordLengthSmallThrowsValidationException() {
        UserRequest userRequest = UserRequest.builder()
                .username("User")
                .password("pa")
                .build();

        assertThatThrownBy(() -> userService.register(userRequest))
                .isInstanceOf(ValidationException.class);

        verify(userDAO, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Авторизация, успешная авторизация пользователя")
    void login_LoginSuccess() {
        UserRequest userRequest = UserRequest.builder()
                .username("user1")
                .password("user1")
                .build();

        User user = User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .build();

        when(userDAO.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        userService.login(userRequest);
    }
}