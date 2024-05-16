package ru.panov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.panov.config.TestConfig;
import ru.panov.model.dto.request.BurningCaloriesRequest;
import ru.panov.model.dto.request.TrainingRequest;
import ru.panov.model.dto.response.TrainingResponse;
import ru.panov.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.panov.util.PathConstants.*;

@SpringBootTest(classes = {TestConfig.class})
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
class TrainingControllerTest {
    private static final TrainingResponse TRAINING_RESPONSE =
            TrainingResponse.builder()
                    .additionalInformation("4км")
                    .trainingId(1L)
                    .timeTraining(LocalTime.of(1, 0, 0))
                    .dateTraining(LocalDateTime.now())
                    .countCalories(302d)
                    .userId(2L)
                    .typeId(1L)
                    .build();

    private static final TrainingRequest TRAINING_REQUEST1 = TrainingRequest.builder()
            .typeId(2L)
            .timeTraining("10:00:00")
            .countCalories(200.0)
            .additionalInformation("Test training")
            .build();
    private static final List<TrainingResponse> TRAINING_RESPONSE_LIST = List.of(TRAINING_RESPONSE);

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("?????????? ??????????, ???????")
    void updateTraining_Success() throws Exception {
        mockMvc.perform(put(TRAINING_PATH + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TRAINING_REQUEST1)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("???????? ??????????, ???????")
    void deleteTraining_Success() throws Exception {
        mockMvc.perform(delete(TRAINING_PATH + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("???????? ??????????, ???????")
    void deleteTraining_idIsNOtValid_ReturnsNotFoundException() throws Exception {
        int trainingId = 5;
        mockMvc.perform(delete(TRAINING_PATH + "/{id}", trainingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isNotFound(),
                        content().json("""
                                {
                                "detail": "Тренировка с id=5 у пользователя с id=2 не найдена"
                                }
                                """));
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("????????? ???? ??????????")
    void getAllTrainings_ReturnsTrainingList() throws Exception {
        mockMvc.perform(get(TRAINING_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(TRAINING_RESPONSE_LIST))
                );


    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("????????? ?????????? ?? ?? id")
    void getTrainingById_ReturnsTraining() throws Exception {
        int trainingId = 1;
        mockMvc.perform(get(TRAINING_PATH + "/{id}", trainingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(TRAINING_RESPONSE))
                );
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("????????? ???? ????? ??????????")
    void getALlTrainingTypes_ReturnsTrainingTypeList() throws Exception {
        mockMvc.perform(get(TRAINING_PATH + TRAINING_TYPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                {"id": 1,"type":"Кардио"},
                                {"id": 2,"type":"Силовая тренировка"},
                                {"id": 3,"type":"Йога"}
                                ]"""));
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("????????? ???? ????? ??????????")
    void caloriesSpentOverPeriod_ReturnsCalories() throws Exception {
        BurningCaloriesRequest request =
                BurningCaloriesRequest.builder()
                        .dateTimeStart(DateTimeUtil.parseDateTime(LocalDateTime.now().minusDays(2)))
                        .dateTimeEnd(DateTimeUtil.parseDateTime(LocalDateTime.now().plusDays(2)))
                        .build();
        mockMvc.perform(post(TRAINING_PATH + TRAINING_BURNED_CALORIES_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(String.valueOf(302.0)));
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("????????? ???? ????? ??????????")
    void caloriesSpentOverPeriod_InvalidParesDateTime_ReturnsBadRequest() throws Exception {
        String dateTimeStart = "01-01-2024 00-00";
        String dateTimeEnd = "01-02-2024 00-00";
        BurningCaloriesRequest request =
                BurningCaloriesRequest.builder()
                        .dateTimeStart(dateTimeStart)
                        .dateTimeEnd(dateTimeEnd)
                        .build();
        mockMvc.perform(post(TRAINING_PATH + TRAINING_BURNED_CALORIES_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest(),
                        content().json("""
                                {
                                "detail": "Неверный ввод времени, даты"
                                }
                                """));
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("????????? ???? ????? ??????????")
    void createTraining_ResultTrainingResponse() throws Exception {
        mockMvc.perform(post(TRAINING_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TRAINING_REQUEST1)))
                .andExpectAll(status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "http://localhost" + TRAINING_PATH + "/2"),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(TRAINING_REQUEST1)));
    }

    @Test
    @DisplayName("????????? ???? ????? ??????????")
    void createTraining_UserIsNotAuthorized_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(post(TRAINING_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TRAINING_REQUEST1)))
                .andExpectAll(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "admin")
    @DisplayName("????????? ???? ??????? ??????")
    void createTraining_BadGrantedAuthorities_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(post(TRAINING_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TRAINING_REQUEST1)))
                .andExpectAll(status().isUnauthorized(),
                        content().json("""
                                {
                                "detail":"Access Denied"
                                }
                                """));
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("????????? ???? ????? ??????????")
    void createTraining_HadTrainingToday_ReturnsBadRequest() throws Exception {
        TrainingRequest trainingRequest = TrainingRequest.builder()
                .typeId(1L)
                .timeTraining("10:00:00")
                .countCalories(200.0)
                .additionalInformation("Test training")
                .build();
        mockMvc.perform(post(TRAINING_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainingRequest)))
                .andExpectAll(status().isBadRequest(),
                        content().json("""
                                {
                                "detail": "Тренировка с данным типом сегодня уже была"
                                }
                                """));
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("????????? ???? ????? ??????????")
    void createTraining_CaloriesLessThanZero_ReturnsBadRequest() throws Exception {
        TrainingRequest trainingRequest = TrainingRequest.builder()
                .typeId(2L)
                .timeTraining("10:00:00")
                .countCalories(-2d)
                .additionalInformation("Test training")
                .build();
        mockMvc.perform(post(TRAINING_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainingRequest)))
                .andExpectAll(status().isBadRequest(),
                        content().json("""
                                {
                                "detail": "Количество потраченных калорий должно быть больше 0."
                                }
                                """));
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("????????? ???? ????? ??????????")
    void createTraining_InvalidParseTime_ReturnsBadRequest() throws Exception {
        TrainingRequest trainingRequest = TrainingRequest.builder()
                .typeId(2L)
                .timeTraining("10.00.00")
                .countCalories(10d)
                .additionalInformation("Test training")
                .build();
        mockMvc.perform(post(TRAINING_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainingRequest)))
                .andExpectAll(status().isBadRequest(),
                        content().json("""
                                {
                                "detail": "Неверный ввод времени, даты"
                                }
                                """));
    }

    @Test
    @WithUserDetails(value = "admin")
    @DisplayName("????????? ???? ????? ??????????")
    void createTrainingType_ResultTrainingTypeResponse() throws Exception {
        mockMvc.perform(post(TRAINING_PATH + TRAINING_TYPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "type":"Бокс"
                                }
                                """))
                .andExpectAll(status().isCreated(),
                        header().string(HttpHeaders.LOCATION,
                                "http://localhost" + TRAINING_PATH + TRAINING_TYPE_PATH + "/4"),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                "id": 4, "type":"Бокс"
                                }
                                """));
    }
}