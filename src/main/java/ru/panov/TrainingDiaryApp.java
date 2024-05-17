package ru.panov;

import annotations.EnableLogg;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения тренировочного дневника.
 */
@SpringBootApplication
@EnableLogg
public class TrainingDiaryApp {
    public static void main(String[] args) {
        SpringApplication.run(TrainingDiaryApp.class, args);
    }
}