package ru.panov.controller.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.panov.model.Audit;
import ru.panov.model.Role;
import ru.panov.model.User;
import ru.panov.service.AuditService;
import ru.panov.service.factory.ServiceFactory;
import ru.panov.util.JsonUtil;

import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static ru.panov.util.PathUtil.AUDIT_PATH;

/**
 * Сервлет для обработки запросов связанных с аудитом.
 */
@WebServlet(AUDIT_PATH)
@RequiredArgsConstructor
public class AuditServlet extends HttpServlet {
    private final AuditService auditService;

    public AuditServlet() {
        this.auditService = ServiceFactory.getInstance().getAuditService();
    }

    /**
     * Обрабатывает GET запросы для получения данных аудита.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        User user = (User) getServletContext().getAttribute("user");
        if (user.getRole().equals(Role.ADMIN)) {
            resp.setStatus(SC_OK);
            List<Audit> audits = auditService.showAllAudits();
            resp.getWriter().write(JsonUtil.writeValue(audits));
        } else {
            resp.setStatus(SC_FORBIDDEN);
        }
    }
}