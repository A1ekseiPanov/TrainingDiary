package ru.panov.model.dto.response;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JwtTokenResponse implements Serializable {
   private String token;
}