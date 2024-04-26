package ru.panov.controller.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.panov.exception.DuplicateException;
import ru.panov.exception.NotFoundException;
import ru.panov.exception.ValidationException;
import ru.panov.model.User;
import ru.panov.model.dto.TrainingDTO;
import ru.panov.model.dto.response.TrainingResponse;
import ru.panov.service.TrainingService;
import ru.panov.service.factory.ServiceFactory;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static jakarta.servlet.http.HttpServletResponse.*;
import static ru.panov.util.JsonUtil.*;
import static ru.panov.util.PathUtil.TRAINING_PATH;

@WebServlet(urlPatterns = {TRAINING_PATH, TRAINING_PATH + "/*"})
public class TrainingServlet extends HttpServlet {
    private final TrainingService trainingService;

    public TrainingServlet() {
        this.trainingService = ServiceFactory.getInstance().getTrainingService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        User user = (User) getServletContext().getAttribute("user");
        if (Objects.isNull(getId(req))) {
            List<TrainingResponse> trainings = trainingService.findAll(user.getId());
            resp.setStatus(SC_OK);
            resp.getWriter().write(writeValue(trainings));
        } else {
            try {
                TrainingResponse training = trainingService.findById(user.getId(), Long.parseLong(getId(req)));
                resp.setStatus(SC_OK);
                resp.getWriter().write(writeValue(training));
            } catch (NotFoundException e) {
                printMessage("Not Found error", e.getMessage(), SC_NOT_FOUND, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        User user = (User) getServletContext().getAttribute("user");
        String json = readJson(req);
        try {
            TrainingDTO training = readValue(json, TrainingDTO.class);
            trainingService.save(user.getId(), training);
            resp.setStatus(SC_OK);
        } catch (ValidationException e) {
            printMessage("Validation error", e.getMessage(), SC_BAD_REQUEST, resp);
        } catch (NotFoundException e) {
            printMessage("Not Found error", e.getMessage(), SC_NOT_FOUND, resp);
        } catch (IllegalArgumentException e) {
            printMessage("Json error", e.getMessage(), SC_BAD_REQUEST, resp);
        } catch (DuplicateException e) {
            printMessage("Duplicate error", e.getMessage(), SC_BAD_REQUEST, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        User user = (User) getServletContext().getAttribute("user");
        String json = readJson(req);
        try {
            TrainingDTO training = readValue(json, TrainingDTO.class);
            trainingService.update(Long.parseLong(getId(req)), training, user.getId());
            resp.setStatus(SC_NO_CONTENT);
        } catch (ValidationException e) {
            printMessage("Validation error", e.getMessage(), SC_BAD_REQUEST, resp);
        } catch (NotFoundException e) {
            printMessage("Not Found error", e.getMessage(), SC_NOT_FOUND, resp);
        } catch (IllegalArgumentException e) {
            printMessage("Json error", e.getMessage(), SC_BAD_REQUEST, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) getServletContext().getAttribute("user");
        try {
            trainingService.delete(Long.parseLong(getId(req)), user.getId());
            resp.setStatus(SC_NO_CONTENT);
        } catch (NotFoundException e) {
            printMessage("Not Found error", e.getMessage(), SC_NOT_FOUND, resp);
        }
    }

    private static String getId(HttpServletRequest req) {
        String path = req.getPathInfo();
        if (path == null) {
            return null;
        }
        String[] paths = path.split("/");
        return paths[1];
    }
}