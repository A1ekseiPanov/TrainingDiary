package ru.panov.controller;

import lombok.RequiredArgsConstructor;
import ru.panov.model.Training;
import ru.panov.model.TrainingType;
import ru.panov.model.dto.TrainingDTO;
import ru.panov.model.dto.TrainingTypeDTO;
import ru.panov.service.TrainingService;
import ru.panov.service.TrainingTypeService;
import ru.panov.service.UserService;

import java.util.List;

@RequiredArgsConstructor
public class TrainingController {
    private final TrainingService trainingService;
    private final UserService userService;
    private final TrainingTypeService typeTrainingService;

    public List<Training> getAllTraining(){
        return trainingService.findAll(getIdLoggedUser());
    }
    public Training getByTrainingById(Long id){
        return trainingService.findById(getIdLoggedUser(),id);
    }

    public void deleteTraining(Long id){
        trainingService.delete(getIdLoggedUser(),id);
    }

    public Training updateTraining(Long id, TrainingDTO trainingDTO){
        return trainingService.update(id,trainingDTO,getIdLoggedUser());
    }

    public Double caloriesSpentOverPeriod(String dateTimeStart, String dateTimeEnd){
        return trainingService.caloriesSpentOverPeriod(dateTimeStart,dateTimeEnd,getIdLoggedUser());
    }

    public Training createTraining(TrainingDTO trainingDTO){
        return trainingService.save(getIdLoggedUser(),trainingDTO);
    }

    public List<TrainingType> getAllTrainingType(){
        return typeTrainingService.findAll();
    }

    public TrainingType createTypeTraining(TrainingTypeDTO typeDTO){
        return typeTrainingService.save(typeDTO);
    }
    private Long getIdLoggedUser(){
        return userService.getLoggedUser().getId();
    }
}
