package ru.panov.controller.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.panov.exception.InputDataConflictException;
import ru.panov.model.dto.JwtTokenResponse;
import ru.panov.model.dto.UserDTO;
import ru.panov.service.UserService;

import java.io.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServletTest {
    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private LoginServlet loginServlet;

    StringWriter stringWriter = new StringWriter();

    PrintWriter writer = new PrintWriter(stringWriter);
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        stringWriter.flush();
        writer.flush();
    }

    @AfterEach
    void afterEach() throws IOException {
        stringWriter.close();
        writer.close();
    }

    @Test
    @DisplayName("Успешный вход")
    void doPost_testSuccessfulLogin() throws Exception {
        UserDTO userDto = UserDTO.builder()
                .username("username")
                .password("password").build();
        JwtTokenResponse tokenResponse = new JwtTokenResponse("token");

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(userDto))));
        when(userService.login(any())).thenReturn(tokenResponse);
        when(response.getWriter()).thenReturn(writer);

        loginServlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_ACCEPTED);
    }

    @Test
    @DisplayName("Некорректный запрос")
    void doPost_testBadRequest() throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{}")));
        when(userService.login(any())).thenThrow(new IllegalArgumentException("Bad Request"));
        when(response.getWriter()).thenReturn(writer);

        loginServlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Конфликт данных")
    void doPost_testConflict() throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{}")));
        when(userService.login(any())).thenThrow(new InputDataConflictException("Conflict"));
        when(response.getWriter()).thenReturn(writer);

        loginServlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_CONFLICT);
    }
}