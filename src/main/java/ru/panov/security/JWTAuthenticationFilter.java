package ru.panov.security;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.panov.model.User;
import ru.panov.service.UserService;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static ru.panov.util.JsonUtil.printMessage;
import static ru.panov.util.PathUtil.*;

/**
 * Фильтр аутентификации JWT.
 */
@WebFilter(urlPatterns = "/*")
@Component
public class JWTAuthenticationFilter implements Filter {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    @Autowired
    private  JwtService jwtService;
    @Autowired
    private UserService userService;
    private ServletContext servletContext;

    /**
     * Инициализация фильтра.
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.servletContext = filterConfig.getServletContext();
    }

    /**
     * Процесс фильтрации запроса.
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (request.getRequestURI().equals(request.getContextPath() + AUTH_PATH + LOGIN_PATH)
                || request.getRequestURI().equals(request.getContextPath() + AUTH_PATH + REGISTRATION_PATH)) {
            filterChain.doFilter(req, res);
            return;
        }
        String authHeader = request.getHeader(HEADER_STRING);

        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, TOKEN_PREFIX)) {
            printMessage("JWT token", "Missing or invalid JWT token", SC_UNAUTHORIZED, response);
            return;
        }
        try {
            String jwt = authHeader.substring(TOKEN_PREFIX.length());
            String username = jwtService.extractUserName(jwt);

            if (isUserLoggedIn() != null && isUserLoggedIn().getUsername().equals(username)) {
                filterChain.doFilter(req, res);
                return;
            }
            if (StringUtils.isEmpty(username)) {
                printMessage("JWT token", "Invalid JWT token", SC_UNAUTHORIZED, response);
                return;
            }
            if (StringUtils.isNotEmpty(username) && isUserLoggedIn() == null) {
                User user = userService.getByUsername(username);
                if (jwtService.isTokenValid(jwt, user)) {
                    servletContext.setAttribute("user", user);
                }
            } else {
                servletContext.setAttribute("user", null);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            printMessage("JWT token", "Invalid JWT token", SC_UNAUTHORIZED, response);
        }
    }

    /**
     * Проверка аутентифицирован ли пользователь.
     *
     * @return объект пользователя, если он аутентифицирован, иначе - null.
     */
    private User isUserLoggedIn() {
        return (User) servletContext.getAttribute("user");
    }
}