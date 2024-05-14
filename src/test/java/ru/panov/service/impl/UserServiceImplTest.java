package ru.panov.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.panov.dao.UserDAO;
import ru.panov.exception.InputDataConflictException;
import ru.panov.exception.ValidationException;
import ru.panov.model.User;
import ru.panov.model.dto.request.UserRequest;
import ru.panov.model.dto.response.JwtTokenResponse;
import ru.panov.security.JwtService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserDetailsService detailsService;
    @Mock
    private AuthenticationManager authenticationManager;

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

        String jwtToken = "JwtToken";

        when(detailsService.loadUserByUsername(userRequest.getUsername())).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(jwtToken);

        JwtTokenResponse jwt = userService.login(userRequest);

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        verify(jwtService).generateToken(user);
        assertThat(jwt.getToken()).isEqualTo(jwtToken);
    }

    @Test
    @DisplayName("Авторизация, неверное имя пользователя или пароль")
    public void login_BadCredentialsThrowsInvalidCredentials() {
        UserRequest userRequest = UserRequest.builder()
                .username("invalidUsername")
                .password("invalidPassword")
                .build();

        doThrow(new BadCredentialsException("")).when(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequest.getUsername(), userRequest.getPassword()));

        assertThatThrownBy(() -> userService.login(userRequest))
                .isInstanceOf(InputDataConflictException.class)
                .hasMessage("неправильное имя пользователя или пароль");

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));
    }
}