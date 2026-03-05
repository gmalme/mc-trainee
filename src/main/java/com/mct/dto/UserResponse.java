package com.mct.dto;

import com.mct.domain.enums.Role;
import java.time.Instant;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String name,
    String email,
    String username,
    Role role,
    Instant createdAt
) {}
