//package ru.panov.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.ServletConfig;
//import jakarta.servlet.ServletContext;
//import jakarta.servlet.ServletException;
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
//import ru.panov.exception.ValidationException;
//import ru.panov.model.User;
//import ru.panov.model.dto.request.TrainingRequest;
//import ru.panov.model.dto.response.TrainingResponse;
//import ru.panov.service.TrainingService;
//import ru.panov.util.DateTimeUtil;
//
//import java.io.*;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TrainingServletTest {
//    @Mock
//    private TrainingService trainingService;
//
//    @Mock
//    private HttpServletRequest request;
//
//    @Mock
//    private HttpServletResponse response;
//
//    @Mock
//    private ServletContext servletContext;
//
//    @Mock
//    private ServletConfig servletConfig;
//
//    @InjectMocks
//    private TrainingServlet trainingServlet;
//
//    StringWriter stringWriter = new StringWriter();
//    PrintWriter writer = new PrintWriter(stringWriter);
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    @BeforeEach
//    void setUp() throws ServletException {
//        trainingServlet.init(servletConfig);
//        when(trainingServlet.getServletContext())
//                .thenReturn(servletContext);
//        when(servletContext.getAttribute("user"))
//                .thenReturn(User.builder().id(1L).build());
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
//    @DisplayName("Получение всех тренировок")
//    void doGet_testGetAllTrainings() throws Exception {
//        Long userId = 1L;
//        TrainingResponse training = TrainingResponse.builder()
//                .typeId(1L)
//                .timeTraining(DateTimeUtil.parseTime(LocalTime.of(1, 10)))
//                .countCalories(1000d)
//                .additionalInformation("4 км")
//                .build();
//        List<TrainingResponse> trainings = new ArrayList<>(Collections.singletonList(training));
//        when(trainingService.findAll(userId)).thenReturn(trainings);
//        when(response.getWriter()).thenReturn(writer);
//
//        trainingServlet.doGet(request, response);
//
//        verify(trainingService).findAll(any());
//        verify(response).setContentType("application/json");
//        verify(response).setStatus(HttpServletResponse.SC_OK);
//        String exs = objectMapper.writeValueAsString(trainings);
//        writer.flush();
//        String acc = stringWriter.toString().trim();
//        assertEquals(exs, acc);
//    }
//
//    @Test
//    @DisplayName("Успешное создание тренировки")
//    void doPost_testSuccess() throws Exception {
//        TrainingRequest training = TrainingRequest.builder()
//                .typeId(1L)
//                .timeTraining(DateTimeUtil.parseTime(LocalTime.of(1, 10)))
//                .countCalories(1000d)
//                .additionalInformation("4 км")
//                .build();
//        when(request.getReader()).thenReturn(new BufferedReader(
//                new StringReader(objectMapper.writeValueAsString(training))));
//
//        trainingServlet.doPost(request, response);
//
//        verify(response).setContentType("application/json");
//        verify(trainingService).save(anyLong(), any(TrainingRequest.class));
//        verify(response).setStatus(HttpServletResponse.SC_OK);
//    }
//
//    @Test
//    @DisplayName("Ошибка валидации при создании тренировки")
//    void doPost_testValidationError() throws Exception {
//        when(response.getWriter()).thenReturn(writer);
//        TrainingRequest training = TrainingRequest.builder().countCalories(-10d).build();
//        when(request.getReader()).thenReturn(
//                new BufferedReader(
//                        new StringReader(objectMapper.writeValueAsString(training))));
//        doThrow(new ValidationException("Validation error"))
//                .when(trainingService).save(anyLong(), any(TrainingRequest.class));
//
//        trainingServlet.doPost(request, response);
//
//        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
//    }
//
//    @Test
//    @DisplayName("Успешное обновление тренировки")
//    void doPut_testSuccess() throws Exception {
//        TrainingRequest training = TrainingRequest.builder()
//                .typeId(1L)
//                .timeTraining(DateTimeUtil.parseTime(LocalTime.of(1, 10)))
//                .countCalories(1000d)
//                .additionalInformation("4 км")
//                .build();
//        when(request.getReader()).thenReturn(new BufferedReader(
//                new StringReader(objectMapper.writeValueAsString(training))));
//        when(request.getPathInfo()).thenReturn("/1");
//
//        trainingServlet.doPut(request, response);
//
//        verify(response).setContentType("application/json");
//        verify(trainingService).update(eq(1L), any(TrainingRequest.class), anyLong());
//        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
//    }
//
//    @Test
//    @DisplayName("Успешное удаление тренировки")
//    void doDelete_Success() throws Exception {
//        when(request.getPathInfo()).thenReturn("/1");
//
//        trainingServlet.doDelete(request, response);
//
//        verify(trainingService).delete(eq(1L), any(Long.class));
//        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
//    }
//}