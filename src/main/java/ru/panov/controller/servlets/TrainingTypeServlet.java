package ru.panov.controller.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.panov.exception.DuplicateException;
import ru.panov.exception.ValidationException;
import ru.panov.model.Role;
import ru.panov.model.User;
import ru.panov.model.dto.request.TrainingTypeRequest;
import ru.panov.model.dto.response.TrainingTypeResponse;
import ru.panov.service.TrainingTypeService;
import ru.panov.service.factory.ServiceFactory;
import ru.panov.util.PathUtil;

import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.*;
import static ru.panov.util.JsonUtil.*;

/**
 * Сервлет для управления типами тренировок.
 */
@WebServlet(PathUtil.TRAINING_TYPE_PATH)
@RequiredArgsConstructor
public class TrainingTypeServlet extends HttpServlet {
    private final TrainingTypeService typeService;

    public TrainingTypeServlet() {
        this.typeService = ServiceFactory.getInstance().getTrainingTypeService();
    }

    /**
     * Обрабатывает GET запросы для получения списка всех типов тренировок.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setStatus(SC_OK);
        List<TrainingTypeResponse> trainingsType = typeService.findAll();
        resp.getWriter().write(writeValue(trainingsType));
    }

    /**
     * Обрабатывает POST запросы для добавления новых типов тренировок.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        User user = (User) getServletContext().getAttribute("user");

        String json = readJson(req);
        try {
            TrainingTypeRequest trainingType = readValue(json, TrainingTypeRequest.class);
            if (user.getRole().equals(Role.ADMIN)) {
                typeService.save(trainingType);
                resp.setStatus(SC_OK);
            } else {
                resp.setStatus(SC_FORBIDDEN);
            }
        } catch (ValidationException e) {
            printMessage("Validation error", e.getMessage(), SC_BAD_REQUEST, resp);
        } catch (IllegalArgumentException e) {
            printMessage("Json error", e.getMessage(), SC_BAD_REQUEST, resp);
        } catch (DuplicateException e) {
            printMessage("Duplicate error", e.getMessage(), SC_BAD_REQUEST, resp);
        }
    }
}