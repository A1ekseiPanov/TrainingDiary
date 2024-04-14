package ru.panov.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.panov.dao.UserDAO;
import ru.panov.model.User;
import ru.panov.model.dto.UserDTO;
import ru.panov.service.AuditService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    void testRegisterSuccess() {
        String username = "user1";
        String password = "user1";
        UserDTO userDTO = UserDTO.builder()
                .username(username)
                .password(password)
                .build();
        User user = User.builder()
                .id(1L)
                .username(username)
                .password(password)
                .registrationDate(LocalDateTime.now())
                .build();
        when(userDAO.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.register(userDTO);

        assertThat(user).isEqualTo(registeredUser);
        assertThat(username).isEqualTo(registeredUser.getUsername());
        assertThat(password).isEqualTo(registeredUser.getPassword());
        verify(auditService).audit(anyString(), anyString(), any(), anyString());

    }

    @Test
    void testLoginSuccess() {
        String username = "user1";
        String password = "user1";
        UserDTO userDTO = UserDTO.builder()
                .username(username)
                .password(password)
                .build();
        User user = User.builder()
                .id(1L)
                .username(username)
                .password(password)
                .registrationDate(LocalDateTime.now())
                .build();
        when(userDAO.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        userService.login(userDTO);
        User loggedUser = userService.getLoggedUser();
        assertThat(user).isEqualTo(loggedUser);
        verify(auditService).audit(anyString(), anyString(), any(), anyString());
    }
}