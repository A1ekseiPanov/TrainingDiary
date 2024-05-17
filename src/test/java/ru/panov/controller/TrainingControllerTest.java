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
import ru.panov.util.DateTimeUtil;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.panov.util.PathConstants.*;

@SpringBootTest(classes = {TestConfig.class})
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
class TrainingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("Успешное обновление тренировки")
    void updateTraining_Success() throws Exception {
        mockMvc.perform(put(TRAINING_PATH + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "typeId": 2,
                                  "countCalories": 200,
                                  "timeTraining": "01:00:00",
                                  "additionalInformation": "Test training"
                                }
                                """))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("Успешное удаление тренировки")
    void deleteTraining_Success() throws Exception {
        mockMvc.perform(delete(TRAINING_PATH + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("Удаление тренировки, id невалиден")
    void deleteTraining_idIsNotValid_ReturnsNotFoundException() throws Exception {
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
    @DisplayName("Успешное получение всех тренировок")
    void getAllTrainings_ReturnsTrainingList() throws Exception {
        mockMvc.perform(get(TRAINING_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                {
                                  "trainingId": 1,
                                  "typeId": 1,
                                  "countCalories": 302,
                                  "timeTraining": "01:00:00",
                                  "additionalInformation": "4км",
                                  "userId": 2
                                }
                                ]
                                """)
                );
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("Успешное получение тренировки по id")
    void getTrainingById_ReturnsTraining() throws Exception {
        int trainingId = 1;
        mockMvc.perform(get(TRAINING_PATH + "/{id}", trainingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                  "trainingId": 1,
                                  "typeId": 1,
                                  "countCalories": 302,
                                  "timeTraining": "01:00:00",
                                  "additionalInformation": "4км",
                                  "userId": 2
                                }
                                """)
                );
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("Успешное получение всех типов тренировок")
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
    @DisplayName("Успешное получение калориий потраченных за период")
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
    @DisplayName("Получение калориий потраченных за период с пустыми данными")
    void caloriesSpentOverPeriod_IsBlankRequest_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post(TRAINING_PATH + TRAINING_BURNED_CALORIES_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                }"""))
                .andExpectAll(status().isBadRequest(),
                        content().json("""
                                {
                                "errors": [
                                      "dateTimeEnd не может быть пустым или состоять только из пробелов",
                                      "dateTimeStart не может быть пустым или состоять только из пробелов"
                                    ]
                                }"""));
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("Получение калориий потраченных за период с невалидными данными")
    void caloriesSpentOverPeriod_InvalidDateRequest_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post(TRAINING_PATH + TRAINING_BURNED_CALORIES_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                   "dateTimeStart": "дд.ММ.гггг чч:мм",
                                   "dateTimeEnd": "дд.ММ.гггг чч:мм"
                                }"""))
                .andExpectAll(status().isBadRequest(),
                        content().json("""
                                {
                                "errors": [
                                       "dateTimeStart дожно соответствовать условию: дд.ММ.гггг чч:мм. Пример: 01.01.2000 10:10",
                                       "dateTimeEnd дожно соответствовать условию: дд.ММ.гггг чч:мм. Пример: 01.01.2000 10:11"
                                    ]
                                }"""));
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("Успешное создание тренировки")
    void createTraining_ResultTrainingResponse() throws Exception {
        mockMvc.perform(post(TRAINING_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "typeId": 2,
                                    "countCalories": 200,
                                    "timeTraining": "01:00:00",
                                    "additionalInformation": "Test training"
                                }
                                """))
                .andExpectAll(status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "http://localhost" + TRAINING_PATH + "/2"),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "trainingId": 2,
                                    "typeId": 2,
                                    "countCalories": 200,
                                    "timeTraining": "01:00:00",
                                    "additionalInformation": "Test training"
                                }
                                """));
    }

    @Test
    @DisplayName("Создание тренировки, пользователь не авторизован")
    void createTraining_UserIsNotAuthorized_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(post(TRAINING_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "typeId": 2,
                                    "countCalories": 200,
                                    "timeTraining": "01:00:00",
                                    "additionalInformation": "Test training"
                                }
                                """))
                .andExpectAll(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "admin")
    @DisplayName("Создание тренировки с неподходящей ролью пользователя")
    void createTraining_BadGrantedAuthorities_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(post(TRAINING_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "typeId": 2,
                                    "countCalories": 200,
                                    "timeTraining": "01:00:00",
                                    "additionalInformation": "Test training"
                                }
                                """))
                .andExpectAll(status().isUnauthorized(),
                        content().json("""
                                {
                                "detail":"Access Denied"
                                }
                                """));
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("Создание тренировки, тренировка с данным типом уже была сегодня")
    void createTraining_HadTrainingToday_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post(TRAINING_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "typeId": 1,
                                    "countCalories": 200,
                                    "timeTraining": "01:00:00",
                                    "additionalInformation": "Test training"
                                }
                                """))
                .andExpectAll(status().isBadRequest(),
                        content().json("""
                                {
                                "detail": "Тренировка с данным типом сегодня уже была"
                                }
                                """));
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("Создание тренировки с пустыми данными")
    void createTraining_IsBlankRequest_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post(TRAINING_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                }"""))
                .andExpectAll(status().isBadRequest(),
                        content().json("""
                                {
                                "errors": [
                                      "timeTraining не может быть пустым или состоять только из пробелов",
                                      "typeId не может быть null",
                                      "countCalories не может быть null"
                                    ]
                                }"""));
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("Создание тренировки с невалидными данными")
    void createTraining_InvalidRequest_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post(TRAINING_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                   "typeId": 0,
                                   "countCalories": 0,
                                   "timeTraining": "чч:мм:сс",
                                   "additionalInformation": "string"
                                 }"""))
                .andExpectAll(status().isBadRequest(),
                        content().json("""
                                {
                                "errors": [
                                      "countCalories должено быть больше 0",
                                      "typeId должен быть больше 0",
                                      "timeTraining должно соответствовать: чч:мм:сс"
                                    ]
                                }"""));
    }

    @Test
    @WithUserDetails(value = "admin")
    @DisplayName("Успешное создание типа тренировки")
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

    @Test
    @WithUserDetails(value = "admin")
    @DisplayName("Создание типа тренировки с пустым типом")
    void createTrainingType_TypeInBlank_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post(TRAINING_PATH + TRAINING_TYPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                }"""))
                .andExpectAll(status().isBadRequest(),
                        content().json("""
                                {
                                "errors": [
                                      "type не может быть пустым или состоять только из пробелов"
                                    ]
                                }"""));
    }

    @Test
    @WithUserDetails(value = "admin")
    @DisplayName("Создание типа тренировки с коротким типа")
    void createTrainingType_TypeSizeSmall_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post(TRAINING_PATH + TRAINING_TYPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "type":"Б"
                                }"""))
                .andExpectAll(status().isBadRequest(),
                        content().json("""
                                {
                                "errors": [
                                      "Длина type должна составлять минимум от 2 символов"
                                    ]
                                }"""));
    }
}