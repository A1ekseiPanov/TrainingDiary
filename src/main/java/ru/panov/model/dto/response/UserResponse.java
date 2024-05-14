package ru.panov.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.panov.model.Role;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse implements Serializable {
    private Long id;
    private String username;
    private Role role;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime created;
}
