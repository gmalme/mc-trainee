package com.mct.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Credenciais de login.
 */
public record LoginRequestDTO(
    @NotBlank String username,
    @NotBlank String password
) {}
