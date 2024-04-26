package ru.panov.controller.servlets;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.panov.exception.InputDataConflictException;
import ru.panov.model.dto.JwtTokenResponse;
import ru.panov.model.dto.UserDTO;
import ru.panov.service.UserService;
import ru.panov.service.factory.ServiceFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Stream;

import static jakarta.servlet.http.HttpServletResponse.*;
import static ru.panov.util.JsonUtil.*;
import static ru.panov.util.PathUtil.LOGIN_PATH;

@WebServlet(LOGIN_PATH)
public class LoginServlet extends HttpServlet {
    private final UserService userService;

    public LoginServlet() {
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
            UserDTO userDto = readValue(json, UserDTO.class);
            JwtTokenResponse tokenResponse = userService.login(userDto);
            resp.setStatus(SC_ACCEPTED);
            resp.getWriter().write(writeValue(tokenResponse));
        } catch (IllegalArgumentException e) {
            printMessage("error", e.getMessage(), SC_BAD_REQUEST, resp);
        } catch (InputDataConflictException e) {
            printMessage("error", e.getMessage(), SC_CONFLICT, resp);
        }
    }
}