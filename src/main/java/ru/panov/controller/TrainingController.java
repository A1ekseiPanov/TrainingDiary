package ru.panov.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import static ru.panov.util.PathConstants.*;

/**
 * Контроллер для управления тренировками и типами тренировок.
 */
@RestController
@RequestMapping(value = TRAINING_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;

    /**
     * Получает все тренировки пользователя.
     *
     * @param user аутентифицированный пользователь
     * @return список тренировок пользователя
     */
    @Operation(
            summary = "Все тренировки пользователя"
    )
    @GetMapping
    public List<TrainingResponse> getAll(@AuthenticationPrincipal User user) {
        return trainingService.findAll(user.getId());
    }

    /**
     * Получает тренировку пользователя по ее ID.
     *
     * @param id   идентификатор тренировки
     * @param user аутентифицированный пользователь
     * @return тренировка пользователя
     */
    @Operation(
            summary = "Тренировка пользователя",
            description = "Тренировка пользователя по ее id"
    )
    @GetMapping("/{id}")
    public TrainingResponse getById(@PathVariable("id") Long id,
                                    @AuthenticationPrincipal User user) {
        return trainingService.findById(user.getId(), id);
    }

    /**
     * Создает новую тренировку.
     *
     * @param trainingRequest      тело запроса на создание тренировки
     * @param uriComponentsBuilder построитель компонентов URI
     * @param user                 аутентифицированный пользователь
     * @return ответ с созданной тренировкой
     */
    @Operation(
            summary = "Добавить новую тренировку"
    )
    @PreAuthorize("hasAuthority('USER')")
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

    /**
     * Обновляет существующую тренировку.
     *
     * @param id              идентификатор тренировки
     * @param trainingRequest тело запроса для обновления тренировки
     * @param user            аутентифицированный пользователь
     * @return ответ без содержимого
     */
    @Operation(
            summary = "Обновить тренировку",
            description = "Обновить тренировку по ее id"
    )
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateTraining(@PathVariable("id") Long id,
                                               @RequestBody TrainingRequest trainingRequest,
                                               @AuthenticationPrincipal User user) {
        trainingService.update(id, trainingRequest, user.getId());
        return ResponseEntity.noContent()
                .build();
    }

    /**
     * Удаляет существующую тренировку.
     *
     * @param id   идентификатор тренировки
     * @param user аутентифицированный пользователь
     * @return ответ без содержимого
     */
    @Operation(
            summary = "Удалить тренировку",
            description = "Удалить тренировку по ее id"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTraining(@PathVariable("id") Long id,
                                               @AuthenticationPrincipal User user) {
        trainingService.delete(user.getId(), id);
        return ResponseEntity.noContent()
                .build();
    }

    /**
     * Получает количество потраченных калорий за выбранный период.
     *
     * @param request тело запроса с периодом для расчета калорий
     * @param user    аутентифицированный пользователь
     * @return количество потраченных калорий
     */
    @Operation(
            summary = "Количество потраченных калорий за выбранный период",
            description = "Количество потраченных калорий за выбранный период дат и времени"
    )
    @PostMapping(TRAINING_BURNED_CALORIES_PATH)
    public Double getCaloriesSpentOverPeriod(@RequestBody BurningCaloriesRequest request,
                                             @AuthenticationPrincipal User user) {
        return trainingService.caloriesSpentOverPeriod(request, user.getId());
    }

    /**
     * Получает все типы тренировок.
     *
     * @return список типов тренировок
     */
    @Operation(
            summary = "Все типы тренировок"
    )
    @GetMapping(TRAINING_TYPE_PATH)
    public List<TrainingTypeResponse> getAllTrainingTypes() {
        return trainingTypeService.findAll();
    }

    /**
     * Создает новый тип тренировки.
     *
     * @param trainingTypeRequest  тело запроса для создания типа тренировки
     * @param uriComponentsBuilder построитель компонентов URI
     * @return ответ с созданным типом тренировки
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Добавить новый тип тренировки"
    )
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