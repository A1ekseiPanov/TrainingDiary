package ru.panov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.panov.model.User;
import ru.panov.model.dto.request.BurningCaloriesRequest;
import ru.panov.model.dto.request.TrainingRequest;
import ru.panov.model.dto.request.TrainingTypeRequest;
import ru.panov.model.dto.response.TrainingResponse;
import ru.panov.model.dto.response.TrainingTypeResponse;
import ru.panov.service.TrainingService;
import ru.panov.service.TrainingTypeService;

import java.util.List;
import java.util.Map;

import static ru.panov.util.PathUtil.*;

@RestController
@RequestMapping(value = TRAINING_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;

    @GetMapping
    public List<TrainingResponse> getAll(@AuthenticationPrincipal User user) {
        return trainingService.findAll(user.getId());
    }

    @GetMapping("/{id}")
    public TrainingResponse getById(@PathVariable("id") Long id,
                                    @AuthenticationPrincipal User user) {
        return trainingService.findById(user.getId(), id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TrainingResponse> createTraining(@RequestBody TrainingRequest trainingRequest,
                                                           UriComponentsBuilder uriComponentsBuilder,
                                                           @AuthenticationPrincipal User user) {
        TrainingResponse training = trainingService.save(user.getId(), trainingRequest);
        return ResponseEntity.created(uriComponentsBuilder
                        .replacePath(TRAINING_PATH + "/{trainingId}")
                        .build(Map.of("trainingId", training.getTrainingId())))
                .body(training);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateTraining(@PathVariable("id") Long id,
                                               @RequestBody TrainingRequest trainingRequest,
                                               @AuthenticationPrincipal User user) {
        trainingService.update(id, trainingRequest, user.getId());
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTraining(@PathVariable("id") Long id,
                                               @AuthenticationPrincipal User user) {
        trainingService.delete(user.getId(), id);
        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping(TRAINING_BURNED_CALORIES_PATH)
    public Double getCaloriesSpentOverPeriod(@RequestBody BurningCaloriesRequest request,
                                             @AuthenticationPrincipal User user) {
        return trainingService.caloriesSpentOverPeriod(request, user.getId());
    }

    @GetMapping(TRAINING_TYPE_PATH)
    public List<TrainingTypeResponse> getById() {
        return trainingTypeService.findAll();
    }

    @PostMapping(value = TRAINING_TYPE_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TrainingTypeResponse> createTrainingType(@RequestBody TrainingTypeRequest trainingTypeRequest,
                                                                   UriComponentsBuilder uriComponentsBuilder) {
        TrainingTypeResponse trainingTypeResponse = trainingTypeService.save(trainingTypeRequest);
        return ResponseEntity.created(uriComponentsBuilder
                        .replacePath(TRAINING_PATH + TRAINING_TYPE_PATH + "/{trainingTypeId}")
                        .build(Map.of("trainingTypeId", trainingTypeResponse.getId())))
                .body(trainingTypeResponse);
    }
}