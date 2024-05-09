package ru.panov.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.panov.annotations.Audit;
import ru.panov.dao.UserDAO;
import ru.panov.exception.InputDataConflictException;
import ru.panov.exception.NotFoundException;
import ru.panov.exception.ValidationException;
import ru.panov.mapper.UserMapper;
import ru.panov.model.User;
import ru.panov.model.dto.request.UserRequest;
import ru.panov.model.dto.response.JwtTokenResponse;
import ru.panov.model.dto.response.UserResponse;
import ru.panov.security.JwtService;
import ru.panov.service.UserService;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Реализация сервиса для работы с пользователями.
 */
@Service
@RequiredArgsConstructor
@Audit
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService detailsService;

    private static final UserMapper MAPPER = UserMapper.INSTANCE;

    @Override
    public UserResponse register(UserRequest userRequest) {
        if (isBlank(userRequest.getUsername()) || isBlank(userRequest.getPassword())) {
            throw new ValidationException("Username и password не могут быть пустыми или состоять только из пробелов.");
        }

        if (userRequest.getPassword().length() < 5 || userRequest.getPassword().length() > 30) {
            throw new ValidationException("Длина пароля должна составлять от 5 до 30 символов.");
        }

        Optional<User> currentUser = userDAO.findByUsername(userRequest.getUsername());

        if (currentUser.isPresent()) {
            throw new InputDataConflictException("Такой пользователь уже существует");
        }

        User newUSer = User.builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build();
        return MAPPER.toResponseEntity(userDAO.save(newUSer));
    }

    @Override
    public JwtTokenResponse login(UserRequest userRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userRequest.getUsername(),
                            userRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new InputDataConflictException("неправильное имя пользователя или пароль");
        }
        UserDetails userDetails = detailsService.loadUserByUsername(userRequest.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);
        return JwtTokenResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public User getById(Long id) {
        return userDAO.findById(id).orElseThrow(
                () -> new NotFoundException("User by id '%s' not found".formatted(id)));
    }
}