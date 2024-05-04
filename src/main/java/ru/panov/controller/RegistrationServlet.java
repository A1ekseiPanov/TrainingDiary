//package ru.panov.controller;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import ru.panov.exception.InputDataConflictException;
//import ru.panov.exception.ValidationException;
//import ru.panov.model.dto.UserDTO;
//import ru.panov.service.UserService;
//
//import java.io.IOException;
//
//import static jakarta.servlet.http.HttpServletResponse.*;
//import static ru.panov.util.JsonUtil.*;
//import static ru.panov.util.PathUtil.REGISTRATION_PATH;
//
///**
// * Сервлет для обработки запросов регистрации новых пользователей.
// */
//@WebServlet(REGISTRATION_PATH)
//@RequiredArgsConstructor
//@Controller
//public class RegistrationServlet extends HttpServlet {
//    private final UserService userService;
//
//    /**
//     * Обрабатывает POST запросы для регистрации новых пользователей.
//     */
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("application/json");
//
//        String json = readJson(req);
//        try {
//            UserDTO userDTO = readValue(json, UserDTO.class);
//            userService.register(userDTO);
//            printMessage("message", "Пользователь " +
//                    userDTO.getUsername() + " успешно зарегистрирован", SC_CREATED, resp);
//        } catch (ValidationException e) {
//            printMessage("validation errors", e.getMessage(), SC_BAD_REQUEST, resp);
//        } catch (InputDataConflictException e) {
//            printMessage("error", e.getMessage(), SC_CONFLICT, resp);
//        } catch (IllegalArgumentException e) {
//            printMessage("JSON errors", e.getMessage(), SC_BAD_REQUEST, resp);
//        }
//    }
//}