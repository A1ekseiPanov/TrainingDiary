package ru.panov.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.panov.dao.UserDAO;

/**
 * Реализация интерфейса UserDetailsService для аутентификации пользователей по их именам пользователя.
 */
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserDAO userDAO;

    /**
     * Загружает пользователя по его имени пользователя.
     *
     * @param username имя пользователя
     * @return UserDetails объект пользователя Spring Security
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        return userDAO.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User ‘" + username + "’ not found"));
    }
}