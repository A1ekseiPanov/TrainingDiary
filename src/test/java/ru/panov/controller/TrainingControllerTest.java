package ru.panov.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.panov.config.WebConfiguration;
import ru.panov.model.dto.response.TrainingResponse;
import ru.panov.model.dto.response.TrainingTypeResponse;
import ru.panov.service.TrainingService;
import ru.panov.service.TrainingTypeService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.panov.util.PathConstants.TRAINING_PATH;
import static ru.panov.util.PathConstants.TRAINING_TYPE_PATH;

@SpringJUnitWebConfig(classes = WebConfiguration.class)
class TrainingControllerTest {
    private MockMvc mockMvc;
    @InjectMocks
    private TrainingController trainingController;
    @Mock
    private TrainingService trainingService;
    @Mock
    private TrainingTypeService trainingTypeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(trainingController)
                .build();
    }

    @Test
    @DisplayName("?????????? ??????????, ???????")
    void updateTraining_Success() throws Exception {
        mockMvc.perform(put(TRAINING_PATH + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("???????? ??????????, ???????")
    void deleteTraining_Success() throws Exception {
        mockMvc.perform(delete(TRAINING_PATH + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("????????? ???? ??????????")
    void getAllTrainings() throws Exception {
        List<TrainingResponse> trainingResponses = new ArrayList<>();
        when(trainingService.findAll(any())).thenReturn(trainingResponses);

        mockMvc.perform(get(TRAINING_PATH).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(trainingService, times(1)).findAll(any());
        verifyNoMoreInteractions(trainingService);
    }

    @Test
    @DisplayName("????????? ?????????? ?? ?? id")
    void getTrainingById() throws Exception {
        TrainingResponse trainingResponse = TrainingResponse.builder().build();
        Long trainingId = 1L;
        when(trainingService.findById(anyLong(), anyLong())).thenReturn(trainingResponse);

        mockMvc.perform(get(TRAINING_PATH + "/{id}", trainingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(trainingService, times(1)).findById(anyLong(), anyLong());
        verifyNoMoreInteractions(trainingService);
    }

    @Test
    @DisplayName("????????? ???? ????? ??????????")
    void getALlTrainingTypes() throws Exception {
        List<TrainingTypeResponse> trainingTypeResponses = new ArrayList<>();
        when(trainingTypeService.findAll()).thenReturn(trainingTypeResponses);

        mockMvc.perform(get(TRAINING_PATH + TRAINING_TYPE_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(trainingTypeService, times(1)).findAll();
        verifyNoMoreInteractions(trainingTypeService);
    }
}