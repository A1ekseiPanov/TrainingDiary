package ru.panov.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.panov.dao.UserDAO;


@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserDAO userDAO;
    @Override
    public UserDetails loadUserByUsername(String username) {
        return userDAO.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User ‘" + username + "’ not found"));
    }
}