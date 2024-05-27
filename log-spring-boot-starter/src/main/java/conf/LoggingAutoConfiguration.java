package conf;

import aspect.LoggableAspect;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import util.EnableMyStarterCondition;

/**
 * Автоматическая конфигурация логгера для стартера.
 */
@Slf4j
@Configuration
@Conditional(EnableMyStarterCondition.class)
public class LoggingAutoConfiguration {
    @Bean
    public LoggableAspect loggableAspect() {
        return new LoggableAspect();
    }

    @PostConstruct
    public void init() {
        log.info("LoggingAutoConfiguration init");
    }
}