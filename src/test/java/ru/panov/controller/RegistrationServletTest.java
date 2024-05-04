//package ru.panov.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ru.panov.exception.InputDataConflictException;
//import ru.panov.exception.ValidationException;
//import ru.panov.model.dto.UserDTO;
//import ru.panov.service.UserService;
//
//import java.io.*;
//
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class RegistrationServletTest {
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private HttpServletRequest request;
//
//    @Mock
//    private HttpServletResponse response;
//
//    @InjectMocks
//    private RegistrationServlet registrationServlet;
//
//    StringWriter stringWriter = new StringWriter();
//    PrintWriter writer = new PrintWriter(stringWriter);
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    @BeforeEach
//    void setUp() {
//        stringWriter.flush();
//        writer.flush();
//    }
//
//    @AfterEach
//    void afterEach() throws IOException {
//        stringWriter.close();
//        writer.close();
//    }
//
//    @Test
//    @DisplayName("Успешная регистрация")
//    void doPost_testSuccessfulRegistration() throws Exception {
//        UserDTO userDto = UserDTO.builder()
//                .username("username")
//                .password("password").build();
//        when(request.getReader()).thenReturn(new BufferedReader(
//                new StringReader(objectMapper.writeValueAsString(userDto))));
//        when(response.getWriter()).thenReturn(writer);
//
//        registrationServlet.doPost(request, response);
//
//        verify(response).setContentType("application/json");
//        verify(response).setStatus(HttpServletResponse.SC_CREATED);
//    }
//
//    @Test
//    @DisplayName("Некорректный запрос")
//    void doPost_testBadRequest() throws Exception {
//        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{}")));
//        doThrow(new ValidationException("Validation errors")).when(userService).register(any());
//        when(response.getWriter()).thenReturn(writer);
//
//        registrationServlet.doPost(request, response);
//
//        verify(response).setContentType("application/json");
//        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
//    }
//
//    @Test
//    @DisplayName("Конфликт данных")
//    void doPost_testConflict() throws Exception {
//        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{}")));
//        doThrow(new InputDataConflictException("Conflict")).when(userService).register(any());
//        when(response.getWriter()).thenReturn(writer);
//
//        registrationServlet.doPost(request, response);
//
//        verify(response).setContentType("application/json");
//        verify(response).setStatus(HttpServletResponse.SC_CONFLICT);
//    }
//}