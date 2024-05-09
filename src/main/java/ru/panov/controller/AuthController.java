package ru.panov.controller;

import io.swagger.v3.oas.annotations.Operation;
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

import static ru.panov.util.PathConstants.*;

/**
 * Контроллер для аутентификации и регистрации пользователей.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = AUTH_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    private final UserService userService;

    /**
     * Вход в систему.
     *
     * @param userRequest тело запроса для входа в систему
     * @return ответ с JWT токеном
     */
    @Operation(
            summary = "Вход в систему",
            description = "Вход в систему, получаем токен для дальнейшей авторизации"
    )
    @PostMapping(value = LOGIN_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtTokenResponse> login(@RequestBody UserRequest userRequest) {
        JwtTokenResponse token = userService.login(userRequest);
        return ResponseEntity.ok(token);
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param userRequest          тело запроса для регистрации
     * @param uriComponentsBuilder построитель компонентов URI
     * @return ответ с данными зарегистрированного пользователя
     */
    @Operation(
            summary = "Регистрация нового пользователя"
    )
    @PostMapping(value = REGISTRATION_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> registration(@RequestBody UserRequest userRequest,
                                                     UriComponentsBuilder uriComponentsBuilder) {
        UserResponse user = userService.register(userRequest);
        return ResponseEntity.created(uriComponentsBuilder.
                        replacePath("users/{userId}")
                        .build(Map.of("userId", user.getId())))
                .body(user);
    }
}