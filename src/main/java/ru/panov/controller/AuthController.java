package ru.panov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.panov.model.dto.JwtTokenResponse;
import ru.panov.model.dto.UserDTO;
import ru.panov.service.UserService;

import static ru.panov.util.PathUtil.AUTH_PATH;
import static ru.panov.util.PathUtil.LOGIN_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = AUTH_PATH)
public class AuthController {
    private final UserService userService;

    @PostMapping(value = LOGIN_PATH, produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtTokenResponse> login(@RequestBody UserDTO userDTO) {
        JwtTokenResponse token = userService.login(userDTO);
        return ResponseEntity.ok(token);
    }
}