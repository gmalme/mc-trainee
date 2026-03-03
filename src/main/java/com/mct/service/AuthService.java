package com.mct.service;

import com.mct.domain.model.User;
import com.mct.dto.LoginRequestDTO;
import com.mct.dto.LoginResponseDTO;
import com.mct.dto.UserDTO;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Arrays;

/**
 * Gerencia autenticação e geração de tokens JWT.
 */
@ApplicationScoped
public class AuthService {

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        User user = User.find("username", loginRequest.username()).firstResult();

        // Verificação simplificada de senha (idealmente usar Argon2 ou BCrypt real)
        if (user == null || !user.passwordHash.equals(loginRequest.password())) {
            throw new WebApplicationException("Credenciais inválidas", Response.Status.UNAUTHORIZED);
        }

        Instant expiry = Instant.now().plus(2, ChronoUnit.HOURS);
        
        String token = Jwt.issuer("mct-issuer")
                .upn(user.username)
                .groups(new HashSet<>(Arrays.asList(user.role.name())))
                .claim("userId", user.id.toString())
                .expiresAt(expiry)
                .sign();

        return new LoginResponseDTO(
                token,
                expiry.getEpochSecond(),
                new UserDTO(user.id, user.username, user.role)
        );
    }
}
