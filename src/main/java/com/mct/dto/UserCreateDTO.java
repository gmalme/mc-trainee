package com.mct.dto;

import com.mct.domain.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Dados necessários para criar ou atualizar um usuário.
 */
public record UserCreateDTO(
    @NotBlank String username,
    @NotBlank String password,
    @NotNull Role role
) {}
