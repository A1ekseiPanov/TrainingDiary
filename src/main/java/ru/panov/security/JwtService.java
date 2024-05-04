package ru.panov.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.panov.model.User;
import ru.panov.util.YamlPropertySourceFactory;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Сервис для работы с JWT (JSON Web Token).
 */
@Service
@PropertySource(value = "classpath:application.yaml", factory = YamlPropertySourceFactory.class)
public class JwtService {
    @Value("jwt.secret")
    private String secret;

    /**
     * Извлекает имя пользователя из JWT.
     *
     * @param token JWT-токен
     * @return имя пользователя
     */
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Проверяет, является ли JWT-токен действительным для указанного пользователя.
     *
     * @param token JWT-токен
     * @param user  пользователь
     * @return true, если токен действителен для пользователя, иначе false
     */
    public boolean isTokenValid(String token, User user) {
        String userName = extractUserName(token);
        return (userName.equals(user.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Извлекает указанное утверждение из JWT.
     *
     * @param token            JWT-токен
     * @param claimsResolvers  функция для извлечения утверждения из объекта Claims
     * @param <T>              тип утверждения
     * @return извлеченное утверждение
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Генерирует JWT-токен для указанного пользователя.
     *
     * @param username имя пользователя
     * @return сгенерированный JWT-токен
     */
    public String generateToken(String username) {
        ClaimsBuilder claims = Jwts.claims().subject(username);
        return Jwts.builder()
                .claims(claims.build())
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Проверяет, истек ли срок действия JWT-токена.
     *
     * @param token JWT-токен
     * @return true, если срок действия токена истек, иначе false
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Извлекает дату истечения срока действия JWT-токена.
     *
     * @param token JWT-токен
     * @return дата истечения срока действия токена
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Извлекает все утверждения (claims) из JWT-токена.
     *
     * @param token JWT-токен
     * @return все утверждения из токена
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Получает секретный ключ для создания и проверки подписи JWT-токена.
     *
     * @return секретный ключ для создания и проверки подписи JWT-токена
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}