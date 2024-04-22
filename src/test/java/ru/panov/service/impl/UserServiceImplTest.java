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
import ru.panov.model.AuditType;
import ru.panov.model.User;
import ru.panov.model.dto.UserDTO;
import ru.panov.service.AuditService;

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
    private AuditService auditService;

    @Test
    @DisplayName("Регистрация, успешная регистрация пользователя")
    void register_ValidUserRegisterSuccess() {
        UserDTO userDTO = UserDTO.builder()
                .username("user1")
                .password("user1")
                .build();
        when(userDAO.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());

        userService.register(userDTO);

        verify(userDAO, times(1)).save(any(User.class));
        verify(auditService, times(1))
                .audit(anyString(), anyString(), any(), anyString());
    }

    @Test
    @DisplayName("Регистрация, пользователь уже существует")
    void register_ExistingUserThrowsInputDataConflictException() {
        UserDTO userDTO = UserDTO.builder()
                .username("existingUser")
                .password("password")
                .build();
        when(userDAO.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(User.builder().build()));

        assertThatThrownBy(() -> userService.register(userDTO))
                .isInstanceOf(InputDataConflictException.class);

        verify(userDAO, never()).save(any(User.class));
        verify(auditService, times(1))
                .audit(anyString(), anyString(), eq(AuditType.FAIL), anyString());
    }

    @Test
    @DisplayName("Регистрация, нулевое имя пользователя вызывает исключение ValidationException")
    void register_NullUsernameThrowsValidationException() {
        UserDTO userDTO = UserDTO.builder()
                .username(null)
                .password("password")
                .build();

        assertThatThrownBy(() -> userService.register(userDTO))
                .isInstanceOf(ValidationException.class);

        verify(userDAO, never()).save(any(User.class));
        verify(auditService, times(1))
                .audit(anyString(), anyString(), eq(AuditType.FAIL), any());
    }

    @Test
    @DisplayName("Регистрация, короткий пароль вызывает исключение ValidationException")
    void register_PasswordLengthSmallThrowsValidationException() {
        UserDTO userDTO = UserDTO.builder()
                .username("User")
                .password("pa")
                .build();

        assertThatThrownBy(() -> userService.register(userDTO))
                .isInstanceOf(ValidationException.class);

        verify(userDAO, never()).save(any(User.class));
        verify(auditService, times(1))
                .audit(anyString(), anyString(), eq(AuditType.FAIL), anyString());
    }

    @Test
    @DisplayName("Авторизация, успешная авторизация пользователя")
    void login_LoginSuccess() {
        UserDTO userDTO = UserDTO.builder()
                .username("user1")
                .password("user1")
                .build();

        User user = User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .build();

        when(userDAO.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        userService.login(userDTO);

        verify(auditService,times(1)).audit(anyString(), anyString(), eq(AuditType.SUCCESS), anyString());
    }
}