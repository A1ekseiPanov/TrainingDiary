package ru.panov.security;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import ru.panov.model.User;
import ru.panov.security.factory.SecurityFactory;
import ru.panov.service.UserService;
import ru.panov.service.factory.ServiceFactory;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static ru.panov.util.JsonUtil.printMessage;
import static ru.panov.util.PathUtil.LOGIN_PATH;
import static ru.panov.util.PathUtil.REGISTRATION_PATH;

@WebFilter(urlPatterns = "/*")
public class JWTAuthenticationFilter implements Filter {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    private final JwtService jwtService;
    private final UserService userService;
    private ServletContext servletContext;

    public JWTAuthenticationFilter() {
        this.jwtService = SecurityFactory.getInstance().getJwtService();
        this.userService = ServiceFactory.getInstance().getUserService();
    }

    @Override
    public void init(FilterConfig filterConfig) {
        this.servletContext = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (request.getRequestURI().equals(request.getContextPath() + LOGIN_PATH)
                || request.getRequestURI().equals(request.getContextPath() + REGISTRATION_PATH) || isUserLoggedIn() != null) {
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

            if (StringUtils.isEmpty(username)) {
                printMessage("JWT token", "Invalid JWT token", SC_UNAUTHORIZED, response);
                return;
            }

            if (StringUtils.isNotEmpty(username) && servletContext.getAttribute("user") == null) {
                User user = userService.getByUsername(username);
                if (jwtService.isTokenValid(jwt, user)) {
                    servletContext.setAttribute("user", user);
                } else {
                    servletContext.setAttribute("user", null);
                }
            } else {
                servletContext.setAttribute("user", null);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            printMessage("JWT token", "Invalid JWT token", SC_UNAUTHORIZED, response);
        }
    }

    private User isUserLoggedIn() {
        return (User) servletContext.getAttribute("user");
    }
}