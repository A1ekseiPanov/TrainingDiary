package ru.panov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.panov.model.TrainingType;
import ru.panov.model.dto.response.TrainingTypeResponse;

import java.util.List;

@Mapper
public interface TrainingTypeMapper {
    TrainingTypeMapper INSTANCE = Mappers.getMapper(TrainingTypeMapper.class);

    TrainingTypeResponse toResponseEntity(TrainingType training);

    List<TrainingTypeResponse> toResponseEntityList(List<TrainingType> trainings);
}