package ru.panov.controller.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.panov.exception.InputDataConflictException;
import ru.panov.exception.ValidationException;
import ru.panov.model.dto.UserDTO;
import ru.panov.service.UserService;
import ru.panov.service.factory.ServiceFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Stream;

import static jakarta.servlet.http.HttpServletResponse.*;
import static ru.panov.util.JsonUtil.printMessage;
import static ru.panov.util.JsonUtil.readValue;
import static ru.panov.util.PathUtil.REGISTRATION_PATH;

@WebServlet(REGISTRATION_PATH)
public class RegistrationServlet extends HttpServlet {
    private final UserService userService;

    public RegistrationServlet() {
        this.userService = ServiceFactory.getInstance().getUserService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader();
             Stream<String> lines = reader.lines()) {
            lines.forEach(sb::append);
        }
        String json = sb.toString();
        try {
            UserDTO userDTO = readValue(json, UserDTO.class);
            userService.register(userDTO);
            printMessage("message", "Пользователь " +
                    userDTO.getUsername() + " успешно зарегистрирован", SC_CREATED, resp);
        } catch (ValidationException e) {
            printMessage("validation errors", e.getMessage(), SC_BAD_REQUEST, resp);
        } catch (InputDataConflictException e) {
            printMessage("error", e.getMessage(), SC_CONFLICT, resp);
        } catch (IllegalArgumentException e) {
            printMessage("JSON errors", e.getMessage(), SC_BAD_REQUEST, resp);
        }
    }
}