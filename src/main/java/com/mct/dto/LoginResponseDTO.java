package com.mct.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * Resposta de login bem-sucedido.
 */
public record LoginResponseDTO(
    String token,
    long expiresAt,
    UserResponse user
) {}
