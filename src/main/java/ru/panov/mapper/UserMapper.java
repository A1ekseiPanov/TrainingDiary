package ru.panov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.panov.model.User;
import ru.panov.model.dto.response.UserResponse;

import java.util.List;

/**
 * Маппер для преобразования сущностей пользователя в DTO и наоборот.
 */
@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


    UserResponse toResponseEntity(User training);


    List<UserResponse> toResponseEntityList(List<User> trainings);
}
