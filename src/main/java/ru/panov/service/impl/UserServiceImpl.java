package ru.panov.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.panov.annotations.Audit;
import ru.panov.dao.UserDAO;
import ru.panov.exception.InputDataConflictException;
import ru.panov.exception.NotFoundException;
import ru.panov.exception.ValidationException;
import ru.panov.model.User;
import ru.panov.model.dto.JwtTokenResponse;
import ru.panov.model.dto.UserDTO;
import ru.panov.security.JwtService;
import ru.panov.service.UserService;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Реализация сервиса для работы с пользователями.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Audit
    public User register(UserDTO userDTO) {
        if (isBlank(userDTO.getUsername()) || isBlank(userDTO.getPassword())) {
            throw new ValidationException("Username и password не могут быть пустыми или состоять только из пробелов.");
        }

        if (userDTO.getPassword().length() < 5 || userDTO.getPassword().length() > 30) {
            throw new ValidationException("Длина пароля должна составлять от 5 до 30 символов.");
        }

        Optional<User> currentUser = userDAO.findByUsername(userDTO.getUsername());

        if (currentUser.isPresent()) {
            throw new InputDataConflictException("Такой пользователь уже существует");
        }

        User newUSer = User.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();
        return userDAO.save(newUSer);
    }

    @Override
    @Audit
    public JwtTokenResponse login(UserDTO userDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDTO.getUsername(),
                            userDTO.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new InputDataConflictException("неправильное имя пользователя или пароль");
        }
        User user = getByUsername(userDTO.getUsername());
        String jwtToken = jwtService.generateToken(user);
        return new JwtTokenResponse(jwtToken);
    }

    @Override
    @Audit(username = "@username")
    public User getByUsername(String username) {
        return userDAO.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User by username '%s' not found".formatted(username)));
    }

    @Override
    @Audit
    public User getById(Long id) {
        return userDAO.findById(id).orElseThrow(
                () -> new NotFoundException("User by id '%s' not found".formatted(id)));
    }
}