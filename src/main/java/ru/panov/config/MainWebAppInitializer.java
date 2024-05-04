package ru.panov.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Класс инициализации веб-приложения.
 * Реализует интерфейс WebApplicationInitializer для настройки приложения при запуске.
 */
public class MainWebAppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext container) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.scan("ru.panov.*");
        container.addListener(new ContextLoaderListener(context));
        ServletRegistration.Dynamic dispatcher = container.addServlet("mvc", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}
