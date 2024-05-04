package ru.panov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.panov.model.TrainingType;
import ru.panov.model.dto.response.TrainingTypeResponse;

import java.util.List;

/**
 * Маппер для преобразования сущностей типов тренировок в DTO ответа и наоборот.
 */
@Mapper
public interface TrainingTypeMapper {
    TrainingTypeMapper INSTANCE = Mappers.getMapper(TrainingTypeMapper.class);

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