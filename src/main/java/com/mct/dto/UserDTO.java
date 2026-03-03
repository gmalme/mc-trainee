package com.mct.dto;

import com.mct.domain.enums.Role;
import java.util.UUID;

/**
 * Representação pública de um usuário para respostas da API.
 */
public record UserDTO(
    UUID id,
    String username,
    Role role
) {}
