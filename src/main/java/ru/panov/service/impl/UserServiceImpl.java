package ru.panov.service.impl;

import lombok.RequiredArgsConstructor;
import ru.panov.dao.UserDAO;
import ru.panov.exception.InputDataConflictException;
import ru.panov.exception.NotFoundException;
import ru.panov.exception.ValidationException;
import ru.panov.model.AuditType;
import ru.panov.model.User;
import ru.panov.service.AuditService;
import ru.panov.service.UserService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final AuditService auditService;

    @Override
    public User register(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty() || username.isBlank() || password.isBlank()) {
            auditService.audit(AuditType.FAIL, "Username и password не могут быть пустыми или состоять только из пробелов.");
            throw new ValidationException("Username и password не могут быть пустыми или состоять только из пробелов.");
        }

        if (password.length() < 5 || password.length() > 30) {
            auditService.audit(AuditType.FAIL, "Длина пароля должна составлять от 5 до 30 символов.");
            throw new ValidationException("Длина пароля должна составлять от 5 до 30 символов.");
        }

        Optional<User> currentUser = userDAO.findByUsername(username);
        if (currentUser.isPresent()) {
            auditService.audit(AuditType.FAIL, "Такой пользователь уже существует");
            throw new InputDataConflictException("Такой пользователь уже существует");
        }

        User newUSer = User.builder()
                .username(username)
                .password(password)
                .build();
        auditService.audit(AuditType.SUCCESS, "Пользователь зарегистрировался успешно");
        return userDAO.save(newUSer);
    }

    @Override
    public Optional<User> authorize(String username, String password)  {
        return Optional.empty();
    }

    @Override
    public User getById(Long id)  {
        return userDAO.findById(id).orElseThrow(() -> new NotFoundException("User with id = %s not found".formatted(id)));
    }

    @Override
    public List<User> showAllUsers() {
        return userDAO.findAll();
    }
}