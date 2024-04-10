package ru.panov.service.impl;

import lombok.RequiredArgsConstructor;
import ru.panov.dao.UserDAO;
import ru.panov.exception.InputDataConflictException;
import ru.panov.exception.NotFoundException;
import ru.panov.exception.ValidationException;
import ru.panov.model.AuditType;
import ru.panov.model.User;
import ru.panov.model.dto.UserDTO;
import ru.panov.service.AuditService;
import ru.panov.service.UserService;

import java.util.Optional;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private User registeredUser;
    private final UserDAO userDAO;
    private final AuditService auditService;

    @Override
    public User register(UserDTO userDTO) {
        if (userDTO.getUsername() == null || userDTO.getPassword() == null ||
                userDTO.getUsername().isEmpty() || userDTO.getPassword().isEmpty()
                || userDTO.getUsername().isBlank() || userDTO.getPassword().isBlank()) {
            auditService.audit(AuditType.FAIL, userDTO.getUsername());
            throw new ValidationException("Username и password не могут быть пустыми или состоять только из пробелов.");
        }

        if (userDTO.getPassword().length() < 5 || userDTO.getPassword().length() > 30) {
            auditService.audit(AuditType.FAIL, userDTO.getUsername());
            throw new ValidationException("Длина пароля должна составлять от 5 до 30 символов.");
        }

        Optional<User> currentUser = userDAO.findByUsername(userDTO.getUsername());
        if (currentUser.isPresent()) {
            auditService.audit(AuditType.FAIL, userDTO.getUsername());
            throw new InputDataConflictException("Такой пользователь уже существует");
        }

        User newUSer = User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .build();
        auditService.audit(AuditType.SUCCESS, userDTO.getUsername());
        return userDAO.save(newUSer);
    }

    @Override
    public void login(UserDTO userDTO) {
        Optional<User> currentUser = userDAO.findByUsername(userDTO.getUsername());
        User loggedUser = getLoggedUser();

        if (loggedUser != null && loggedUser.getUsername().equals(userDTO.getUsername()) && loggedUser.getPassword().equals(userDTO.getPassword())) {
            throw new InputDataConflictException("Вы уже выполнили вход");
        } else if (loggedUser != null) {
            throw new InputDataConflictException("Нельзя войти пока есть залогиненый пользователь: username(" + getLoggedUser().getUsername() + ")");
        }

        if (currentUser.isPresent() && currentUser.get().getPassword().equals(userDTO.getPassword())) {
            registeredUser = currentUser.get();
            auditService.audit(AuditType.SUCCESS, userDTO.getUsername());
        } else {
            auditService.audit(AuditType.FAIL, userDTO.getUsername());
            throw new IllegalArgumentException("Неверное имя пользователя или пароль. Ошибка входа.");
        }
    }


    @Override
    public void logout() {
        if (registeredUser != null) {
            auditService.audit(AuditType.SUCCESS, registeredUser.getUsername());
            registeredUser = null;
        } else {
            throw new NotFoundException("В данный момент ни один пользователь не вошел в систему.");
        }
    }

    @Override
    public User getLoggedUser() {
        return registeredUser;
    }
}