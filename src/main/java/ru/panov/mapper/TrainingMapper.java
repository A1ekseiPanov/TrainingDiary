package ru.panov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.panov.model.Training;
import ru.panov.model.dto.response.TrainingResponse;

import java.util.List;

@Mapper
public interface TrainingMapper {
   TrainingMapper INSTANCE = Mappers.getMapper(TrainingMapper.class);


    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "typeId", target = "typeId")
    @Mapping(source = "countCalories", target = "countCalories")
    @Mapping(source = "trainingTime", target = "timeTraining")
    @Mapping(source = "additionalInfo", target = "additionalInformation")
    @Mapping(source = "id", target = "trainingId")
    @Mapping(source = "created", target = "dateTraining")
    TrainingResponse toResponseDTO(Training training) ;


 List<TrainingResponse> toDtoResponseList(List<Training> trainings);

}
