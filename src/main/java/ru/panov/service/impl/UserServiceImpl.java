package ru.panov.service.impl;


import lombok.RequiredArgsConstructor;
import ru.panov.dao.UserDAO;
import ru.panov.exception.InputDataConflictException;
import ru.panov.exception.NotFoundException;
import ru.panov.exception.ValidationException;
import ru.panov.model.AuditType;
import ru.panov.model.User;
import ru.panov.model.dto.JwtTokenResponse;
import ru.panov.model.dto.UserDTO;
import ru.panov.security.JwtService;
import ru.panov.service.AuditService;
import ru.panov.service.UserService;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Реализация сервиса для работы с пользователями.
 */
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final AuditService auditService;
    private final JwtService jwtService;

    @Override
    public User register(UserDTO userDTO) {
        if (isBlank(userDTO.getUsername()) || isBlank(userDTO.getPassword())) {
            auditService.audit(this.getClass().getSimpleName(), "register", AuditType.FAIL, userDTO.getUsername());
            throw new ValidationException("Username и password не могут быть пустыми или состоять только из пробелов.");
        }

        if (userDTO.getPassword().length() < 5 || userDTO.getPassword().length() > 30) {
            auditService.audit(this.getClass().getSimpleName(), "register", AuditType.FAIL, userDTO.getUsername());
            throw new ValidationException("Длина пароля должна составлять от 5 до 30 символов.");
        }

        Optional<User> currentUser = userDAO.findByUsername(userDTO.getUsername());

        if (currentUser.isPresent()) {
            auditService.audit(this.getClass().getSimpleName(), "register", AuditType.FAIL, userDTO.getUsername());
            throw new InputDataConflictException("Такой пользователь уже существует");
        }

        User newUSer = User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .build();
        auditService.audit(this.getClass().getSimpleName(), "register", AuditType.SUCCESS, userDTO.getUsername());
        return userDAO.save(newUSer);
    }

    @Override
    public JwtTokenResponse login(UserDTO userDTO) {
        Optional<User> currentUser = userDAO.findByUsername(userDTO.getUsername());

        if (currentUser.isPresent() && currentUser.get().getPassword().equals(userDTO.getPassword())) {
            auditService.audit(this.getClass().getSimpleName(), "login", AuditType.SUCCESS, userDTO.getUsername());
            String token = jwtService.generateToken(userDTO.getUsername());
            return new JwtTokenResponse(token);
        } else {
            auditService.audit(this.getClass().getSimpleName(), "login", AuditType.FAIL, userDTO.getUsername());
            throw new IllegalArgumentException("Неверное имя пользователя или пароль. Ошибка входа.");
        }
    }

       @Override
    public User getByUsername(String username) {
        return userDAO.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User by username '%s' not found".formatted(username)));
    }

    @Override
    public User getById(Long id) {
        return userDAO.findById(id).orElseThrow(
                () -> new NotFoundException("User by id '%s' not found".formatted(id)));
    }
}