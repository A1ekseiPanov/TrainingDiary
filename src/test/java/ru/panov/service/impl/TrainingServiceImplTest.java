package ru.panov.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.panov.dao.TrainingDAO;
import ru.panov.exception.DuplicateException;
import ru.panov.exception.NotFoundException;
import ru.panov.model.Role;
import ru.panov.model.Training;
import ru.panov.model.User;
import ru.panov.model.dto.request.TrainingRequest;
import ru.panov.model.dto.request.BurningCaloriesRequest;
import ru.panov.model.dto.response.TrainingResponse;
import ru.panov.service.TrainingTypeService;
import ru.panov.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {
    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private UserService userService;

    @Mock
    private TrainingTypeService typeService;


    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Test
    @DisplayName("Получение всех тренировок админа")
    void findAll_AdminRoleReturnsAllTrainings() {
        User adminUser = User.builder().id(1L).role(Role.ADMIN).username("admin").build();
        when(userService.getById(adminUser.getId())).thenReturn(adminUser);
        when(trainingDAO.findAll()).thenReturn(Collections.emptyList());

        List<TrainingResponse> result = trainingService.findAll(1L);

        verify(userService, times(2)).getById(any());
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Получение всех тренировок пользователя")
    void findAll_UserRoleReturnsUserTrainings() {
        User user = User.builder().id(1L).role(Role.USER).username("user").build();
        when(userService.getById(user.getId())).thenReturn(user);
        when(trainingDAO.findAllByUserId(anyLong())).thenReturn(Collections.emptyList());

        List<TrainingResponse> result = trainingService.findAll(1L);

        verify(userService, times(2)).getById(any());
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Получение тренировки по id у авторезированного пользователя, тренировка найдена")
    void findById_TrainingExistsAndUserLoggedReturnsTraining() {
        Long userId = 1L;
        Long trainingId = 1L;
        User user = User.builder()
                .id(userId)
                .username("user")
                .build();
        Training training = Training.builder()
                .id(trainingId)
                .additionalInfo("инфо")
                .countCalories(100d)
                .userId(userId)
                .build();
        when(userService.getById(userId)).thenReturn(user);
        when(trainingDAO.findById(trainingId, userId)).thenReturn(Optional.of(training));

        TrainingResponse result = trainingService.findById(userId, trainingId);

        verify(userService, times(1)).getById(userId);
        assertThat(result.getAdditionalInformation()).isEqualTo(training.getAdditionalInfo());
        assertThat(result.getUserId()).isEqualTo(training.getUserId());
        assertThat(result.getCountCalories()).isEqualTo(training.getCountCalories());
    }

    @Test
    @DisplayName("Получение тренировки по id у авторезированного пользователя, тренировка не найдена")
    void findById_TrainingDoesNotExistThrowsNotFoundException() {
        Long userId = 1L;
        Long trainingId = 1L;
        User user = User.builder()
                .id(userId)
                .build();
        when(userService.getById(userId)).thenReturn(user);
        when(trainingDAO.findById(trainingId, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainingService.findById(userId, trainingId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Удаление тренировки по id у авторезированного пользователя, тренировка найдена")
    void delete_TrainingExistsAndUserLoggedDeletesTraining() {
        Long userId = 1L;
        Long trainingId = 1L;
        User user = User.builder()
                .id(userId)
                .build();
        Training training = Training.builder()
                .id(trainingId)
                .userId(userId)
                .build();
        when(userService.getById(userId)).thenReturn(user);
        when(trainingDAO.findById(trainingId, userId)).thenReturn(Optional.of(training));

        trainingService.delete(userId, trainingId);

        verify(trainingDAO).delete(trainingId, userId);
    }

    @Test
    @DisplayName("Удаление тренировки по id у авторезированного пользователя, тренировка не найдена")
    void delete_TrainingDoesNotExistThrowsNotFoundException() {
        Long userId = 1L;
        Long trainingId = 1L;
        User user = User.builder().id(userId).build();
        when(userService.getById(userId)).thenReturn(user);
        when(trainingDAO.findById(trainingId, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainingService.delete(userId, trainingId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Обновление тренировки по id у авторезированного пользователя, тренировка найдена")
    public void update_UpdatedTrainingSuccess() {
        Long trainingId = 1L;
        Long userId = 123L;
        TrainingRequest trainingRequest = TrainingRequest.builder()
                .typeId(1L)
                .timeTraining("10:00:00")
                .countCalories(200.0)
                .additionalInformation("Test training")
                .build();
        User user = User.builder()
                .id(userId)
                .username("testUser")
                .build();
        when(trainingDAO.findById(trainingId, userId)).thenReturn(Optional.ofNullable(Training.builder().build()));
        when(userService.getById(userId)).thenReturn(user);

        trainingService.update(trainingId, trainingRequest, userId);

        verify(trainingDAO, times(1)).findById(trainingId, userId);
        verify(userService, times(1)).getById(userId);
        verify(trainingDAO, times(1)).update(any(), eq(userId));
    }

    @Test
    @DisplayName("Обновление тренировки по id у авторезированного пользователя, тренировка не найдена")
    void update_TrainingDoesNotExistThrowsNotFoundException() {
        Long userId = 1L;
        Long trainingId = 1L;
        TrainingRequest trainingRequest = TrainingRequest.builder().countCalories(10d).build();
        when(trainingDAO.findById(trainingId, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainingService.update(trainingId, trainingRequest, userId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Сорханение новой тренировки у авторезированного пользователя")
    public void save_NewTrainingSuccess() {
        Long userId = 123L;
        TrainingRequest trainingRequest = TrainingRequest.builder()
                .typeId(1L)
                .timeTraining("10:00:00")
                .countCalories(200.0)
                .additionalInformation("Test training")
                .build();
        User user = User.builder()
                .id(userId)
                .username("testUser")
                .build();
        when(trainingDAO.findAllByUserId(userId)).thenReturn(Collections.emptyList());
        when(userService.getById(userId)).thenReturn(user);

        trainingService.save(userId, trainingRequest);

        verify(trainingDAO, times(1)).findAllByUserId(userId);
        verify(userService, times(1)).getById(userId);
        verify(trainingDAO, times(1)).save(any(), eq(userId));
    }

    @Test
    @DisplayName("Сорханение новой тренировки у авторезированного пользователя, пользователь уже сохранял тренировку данного типа в данный день")
    void save_UserAlreadyHasTrainingForTypeTodayThrowsDuplicateException() {
        Long userId = 1L;
        TrainingRequest trainingRequest = TrainingRequest.builder().typeId(1L).countCalories(12.2).build();
        List<Training> existingTrainings = Collections.singletonList(Training.builder().userId(userId)
                .typeId(1L).countCalories(12.2).build());
        when(trainingDAO.findAllByUserId(userId)).thenReturn(existingTrainings);

        assertThatThrownBy(() -> trainingService.save(userId, trainingRequest))
                .isInstanceOf(DuplicateException.class);
        verify(trainingDAO, times(1)).findAllByUserId(userId);
    }

    @Test
    @DisplayName("Получение калори израсходованных за период")
    public void caloriesSpentOverPeriod_ReturnsCalories() {
        String dateTimeStart = "01-01-2024 00:00";
        String dateTimeEnd = "01-02-2024 00:00";
        Long userId = 123L;
        Double expectedCalories = 100.0;
        when(trainingDAO.caloriesSpentOverPeriod(any(), any(), anyLong())).thenReturn(expectedCalories);

        Double actualCalories = trainingService.caloriesSpentOverPeriod(new BurningCaloriesRequest(dateTimeStart, dateTimeEnd), userId);

        verify(trainingDAO, times(1)).caloriesSpentOverPeriod(
                any(), any(), eq(userId));
        assertThat(expectedCalories).isEqualTo(actualCalories);
    }
}