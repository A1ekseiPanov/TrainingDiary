package ru.panov.controller.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.panov.model.User;
import ru.panov.model.dto.request.BurningCaloriesRequest;
import ru.panov.service.TrainingService;
import ru.panov.service.factory.ServiceFactory;
import ru.panov.util.PathUtil;

import java.io.IOException;
import java.time.format.DateTimeParseException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static ru.panov.util.JsonUtil.*;

/**
 * Сервлет для расчета потраченных калорий за тренировки в определенный период времени.
 */
@WebServlet(PathUtil.TRAINING_BURNED_CALORIES_PATH)
@RequiredArgsConstructor
public class TrainingCaloriesServlet extends HttpServlet {
    private final TrainingService trainingService;

    public TrainingCaloriesServlet() {
        this.trainingService = ServiceFactory.getInstance().getTrainingService();
    }

    /**
     * Обрабатывает POST запросы для расчета потраченных калорий за тренировки в определенный период времени.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        User user = (User) getServletContext().getAttribute("user");
        String json = readJson(req);
        try {
            BurningCaloriesRequest burningCaloriesRequest = readValue(json, BurningCaloriesRequest.class);
            Double calories = trainingService.caloriesSpentOverPeriod(burningCaloriesRequest, user.getId());
            resp.getWriter().write(writeValue(calories));
        } catch (DateTimeParseException e) {
            printMessage("Parse error", "Incorrect date entry(dd-MM-yyyy HH:mm)", SC_BAD_REQUEST, resp);
        }
    }
}