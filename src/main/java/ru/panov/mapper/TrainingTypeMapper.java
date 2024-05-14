package ru.panov.mapper;

import org.mapstruct.Mapper;
import ru.panov.model.TrainingType;
import ru.panov.model.dto.response.TrainingTypeResponse;

import java.util.List;

/**
 * Маппер для преобразования сущностей типов тренировок в DTO ответа и наоборот.
 */
@Mapper(componentModel = "spring")
public interface TrainingTypeMapper {

    /**
     * Преобразует сущность типа тренировки в DTO ответа.
     *
     * @param training сущность типа тренировки
     * @return DTO ответа на тип тренировки
     */
    TrainingTypeResponse toResponseEntity(TrainingType training);

    /**
     * Преобразует список сущностей типов тренировок в список DTO ответов.
     *
     * @param trainings список сущностей типов тренировок
     * @return список DTO ответов на типы тренировок
     */
    List<TrainingTypeResponse> toResponseEntityList(List<TrainingType> trainings);
}