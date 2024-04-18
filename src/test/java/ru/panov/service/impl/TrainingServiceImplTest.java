package ru.panov.service.impl;

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
import ru.panov.model.dto.TrainingDTO;
import ru.panov.service.AuditService;
import ru.panov.service.TrainingTypeService;
import ru.panov.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {
    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private UserService userService;

    @Mock
    private TrainingTypeService typeService;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Test
    void findAll_AdminRoleReturnsAllTrainings() {
        User adminUser = User.builder().id(1L).role(Role.ADMIN).build();
        when(userService.getLoggedUser()).thenReturn(adminUser);
        when(trainingDAO.findAll()).thenReturn(Collections.emptyList());

        List<Training> result = trainingService.findAll(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_UserRoleReturnsUserTrainings() {
        User user = User.builder().id(1L).role(Role.USER).build();
        when(userService.getLoggedUser()).thenReturn(user);
        when(trainingDAO.findAllByUserId(anyLong())).thenReturn(Collections.emptyList());

        List<Training> result = trainingService.findAll(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void findById_TrainingExistsAndUserLoggedReturnsTraining() {
        Long userId = 1L;
        Long trainingId = 1L;
        User user = User.builder().id(userId).build();
        Training training = Training.builder().id(trainingId).userId(userId).build();
        when(userService.getLoggedUser()).thenReturn(user);
        when(trainingDAO.findById(trainingId, userId)).thenReturn(Optional.of(training));

        Training result = trainingService.findById(userId, trainingId);

        assertThat(result).isEqualTo(training);
    }

    @Test
    void findById_TrainingDoesNotExistThrowsNotFoundException() {
        Long userId = 1L;
        Long trainingId = 1L;
        User user = User.builder().id(userId).build();
        when(userService.getLoggedUser()).thenReturn(user);
        when(trainingDAO.findById(trainingId, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainingService.findById(userId, trainingId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void delete_TrainingExistsAndUserLoggedDeletesTraining() {
        Long userId = 1L;
        Long trainingId = 1L;
        User user = User.builder().id(userId).build();
        Training training = Training.builder().id(trainingId).userId(userId).build();
        when(userService.getLoggedUser()).thenReturn(user);
        when(trainingDAO.findById(trainingId, userId)).thenReturn(Optional.of(training));

        trainingService.delete(userId, trainingId);

        verify(trainingDAO).delete(trainingId, userId);
    }

    @Test
    void delete_TrainingDoesNotExistThrowsNotFoundException() {
        Long userId = 1L;
        Long trainingId = 1L;
        User user = User.builder().id(userId).build();
        when(userService.getLoggedUser()).thenReturn(user);
        when(trainingDAO.findById(trainingId, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainingService.delete(userId, trainingId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void update_TrainingDoesNotExistThrowsNotFoundException() {
        Long userId = 1L;
        Long trainingId = 1L;
        TrainingDTO trainingDTO = TrainingDTO.builder().build();
        when(userService.getLoggedUser()).thenReturn(User.builder().id(userId).build());
        when(trainingDAO.findById(trainingId, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainingService.update(trainingId, trainingDTO, userId))
                .isInstanceOf(NotFoundException.class);
    }


    @Test
    void save_UserAlreadyHasTrainingForTypeTodayThrowsDuplicateException() {
        Long userId = 1L;
        TrainingDTO trainingDTO = TrainingDTO.builder().typeId(1L).build();
        List<Training> existingTrainings = Collections.singletonList(Training.builder()
                .typeId(1L).build());
        when(userService.getLoggedUser()).thenReturn(User.builder().id(userId).build());
        when(trainingDAO.findAllByUserId(userId)).thenReturn(existingTrainings);

        assertThatThrownBy(() -> trainingService.save(userId, trainingDTO))
                .isInstanceOf(DuplicateException.class);
    }
}