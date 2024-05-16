package ru.panov.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.panov.dao.TrainingTypeDAO;
import ru.panov.exception.DuplicateException;
import ru.panov.exception.NotFoundException;
import ru.panov.exception.ValidationException;
import ru.panov.mapper.TrainingTypeMapper;
import ru.panov.model.TrainingType;
import ru.panov.model.dto.request.TrainingTypeRequest;
import ru.panov.model.dto.response.TrainingTypeResponse;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeServiceImplTest {

    private static final TrainingType TYPE1 = TrainingType.builder()
            .id(1L)
            .type("бжж")
            .build();
    private static final TrainingType TYPE2 = TrainingType.builder()
            .id(2L)
            .type("киберспорт")
            .build();
    private static final TrainingTypeRequest TYPE_REQUEST2 = TrainingTypeRequest.builder()
            .type(TYPE2.getType())
            .build();
    private static final TrainingTypeResponse TRAINING_TYPE_RESPONSE2 = TrainingTypeResponse.builder()
            .id(TYPE2.getId())
            .type(TYPE2.getType())
            .build();
    private static final TrainingTypeResponse TRAINING_TYPE_RESPONSE1 = TrainingTypeResponse.builder()
            .id(TYPE1.getId())
            .type(TYPE1.getType())
            .build();
    @Mock
    private TrainingTypeDAO trainingTypeDAO;
    @Mock
    private TrainingTypeMapper mapper;
    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @Test
    void findById_ValidIdReturnsTrainingTypeResponse() {

        when(trainingTypeDAO.findById(TYPE1.getId())).thenReturn(Optional.of(TYPE1));
        when(mapper.toResponseEntity(TYPE1)).thenReturn(TRAINING_TYPE_RESPONSE1);

        TrainingTypeResponse trainingTypeResponse = trainingTypeService.findById(TYPE1.getId());

        assertThat(trainingTypeResponse).isNotNull();
        assertThat(TYPE1.getId()).isEqualTo(trainingTypeResponse.getId());
        assertThat(TYPE1.getType()).isEqualTo(trainingTypeResponse.getType());
        verify(trainingTypeDAO, times(1)).findById(TYPE1.getId());
    }

    @Test
    void findById_InvalidIdReturnsThrowsNotFoundException() {
        Long trainingTypeId = 1L;
        when(trainingTypeDAO.findById(trainingTypeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainingTypeService.findById(trainingTypeId))
                .isInstanceOf(NotFoundException.class);

        verify(trainingTypeDAO, times(1)).findById(1L);
    }

    @Test
    void findAll_ReturnsTrainingTypeResponseList() {
        List<TrainingType> types = List.of(TYPE1, TYPE2);
        List<TrainingTypeResponse> typesR = List.of(TRAINING_TYPE_RESPONSE1, TRAINING_TYPE_RESPONSE2);
        when(trainingTypeDAO.findAll()).thenReturn(types);
        when(mapper.toResponseEntityList(types)).thenReturn(typesR);

        List<TrainingTypeResponse> typeResponses = trainingTypeService.findAll();

        assertThat(typeResponses).isNotEmpty();
        assertThat(typeResponses.size()).isEqualTo(types.size());
        verify(trainingTypeDAO, times(1)).findAll();
    }

    @Test
    void save_ValidTrainingTypeRequestReturnsTrainingTypeResponse() {
        TrainingType type = TrainingType.builder()
                .type(TYPE_REQUEST2.getType())
                .build();
        when(trainingTypeDAO.findByType(TYPE_REQUEST2.getType())).thenReturn(Optional.empty());
        when(trainingTypeDAO.save(any(TrainingType.class))).thenReturn(type);
        when(mapper.toResponseEntity(type)).thenReturn(TRAINING_TYPE_RESPONSE2);

        TrainingTypeResponse typeResponse = trainingTypeService.save(TYPE_REQUEST2);

        assertThat(TYPE_REQUEST2.getType()).isEqualTo(typeResponse.getType());
        verify(trainingTypeDAO, times(1)).save(any());
    }

    @Test
    void save_DuplicateTypeThrowsDuplicateException() {
        when(trainingTypeDAO.findByType(TYPE_REQUEST2.getType())).thenReturn(Optional.of(new TrainingType()));
        assertThatThrownBy(() -> trainingTypeService.save(TYPE_REQUEST2))
                .isInstanceOf(DuplicateException.class);
        verify(trainingTypeDAO, never()).save(any());
    }

    @Test
    void save_BlankTypeThrowsValidationException() {
        TrainingTypeRequest typeRequest = TrainingTypeRequest.builder().type("").build();

        assertThatThrownBy(() -> trainingTypeService.save(typeRequest))
                .isInstanceOf(ValidationException.class);
        verify(trainingTypeDAO, never()).save(any());
    }

    @Test
    void save_ShortTypeThrowsValidationException() {
        TrainingTypeRequest typeRequest = TrainingTypeRequest.builder().type("T").build();

        assertThatThrownBy(() -> trainingTypeService.save(typeRequest))
                .isInstanceOf(ValidationException.class);
        verify(trainingTypeDAO, never()).save(any());
    }
}
