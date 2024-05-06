package ru.panov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import ru.panov.model.dto.response.JwtTokenResponse;
import ru.panov.model.dto.request.UserRequest;
import ru.panov.model.dto.response.UserResponse;
import ru.panov.service.UserService;

import java.util.Map;

import static ru.panov.util.PathUtil.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = AUTH_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    private final UserService userService;

    @PostMapping(value = LOGIN_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtTokenResponse> login(@RequestBody UserRequest userRequest) {
        JwtTokenResponse token = userService.login(userRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = REGISTRATION_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> registration(@RequestBody UserRequest userRequest,
                                                     UriComponentsBuilder uriComponentsBuilder) {
        UserResponse user = userService.register(userRequest);
        return ResponseEntity.created(uriComponentsBuilder.
                        replacePath(AUTH_PATH + REGISTRATION_PATH + "/{userId}")
                        .build(Map.of("userId", user.getId())))
                .body(user);
    }
}