package ru.panov.security.factory;

import lombok.Getter;
import ru.panov.security.JwtService;


public final class SecurityFactory {

    private static final SecurityFactory INSTANCE = new SecurityFactory();

    @Getter
    private final JwtService jwtService = new JwtService();

    private SecurityFactory() {
    }

    public static SecurityFactory getInstance() {
        return INSTANCE;
    }
}