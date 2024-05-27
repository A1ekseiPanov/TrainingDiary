package ru.panov.mapper;

import org.mapstruct.Mapper;
import ru.panov.model.User;
import ru.panov.model.dto.response.UserResponse;

import java.util.List;

/**
 * Маппер для преобразования сущностей пользователя в DTO и наоборот.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponseEntity(User training);

    List<UserResponse> toResponseEntityList(List<User> trainings);
}