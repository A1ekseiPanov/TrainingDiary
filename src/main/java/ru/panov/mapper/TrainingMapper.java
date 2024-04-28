package ru.panov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.panov.model.Training;
import ru.panov.model.dto.response.TrainingResponse;

import java.util.List;

/**
 * Маппер для преобразования сущностей тренировок в DTO и наоборот.
 */
@Mapper
public interface TrainingMapper {
    TrainingMapper INSTANCE = Mappers.getMapper(TrainingMapper.class);
    /**
     * Преобразует сущность тренировки в DTO ответа.
     *
     * @param training сущность тренировки
     * @return DTO тренировки
     */
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "typeId", target = "typeId")
    @Mapping(source = "countCalories", target = "countCalories")
    @Mapping(source = "trainingTime", target = "timeTraining")
    @Mapping(source = "additionalInfo", target = "additionalInformation")
    @Mapping(source = "id", target = "trainingId")
    @Mapping(source = "created", target = "dateTraining")
    TrainingResponse toResponseDTO(Training training);

    /**
     * Преобразует список сущностей тренировок в список DTO ответов.
     *
     * @param trainings список сущностей тренировок
     * @return список DTO ответов на тренировки
     */
    List<TrainingResponse> toDtoResponseList(List<Training> trainings);
}
