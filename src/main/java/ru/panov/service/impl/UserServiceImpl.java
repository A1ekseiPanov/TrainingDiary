package ru.panov.service.impl;


import annotations.Audit;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.panov.dao.UserDAO;
import ru.panov.exception.InputDataConflictException;
import ru.panov.exception.NotFoundException;
import ru.panov.mapper.UserMapper;
import ru.panov.model.User;
import ru.panov.model.dto.request.UserRequest;
import ru.panov.model.dto.response.JwtTokenResponse;
import ru.panov.model.dto.response.UserResponse;
import ru.panov.security.JwtService;
import ru.panov.service.UserService;

import java.util.Optional;

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
    private final UserMapper mapper;

    @Override
    public UserResponse register(UserRequest userRequest) {
        Optional<User> currentUser = userDAO.findByUsername(userRequest.getUsername());

        if (currentUser.isPresent()) {
            throw new InputDataConflictException("Такой пользователь уже существует");
        }

        User newUSer = User.builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build();
        return mapper.toResponseEntity(userDAO.save(newUSer));
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