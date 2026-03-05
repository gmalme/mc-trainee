package com.mct.dto;

import com.mct.domain.enums.TaskStatus;
import java.time.Instant;
import java.util.UUID;

public record TaskResponse(
    UUID id,
    UUID userId,
    String title,
    String description,
    TaskStatus status,
    Instant dueDate,
    Instant createdAt
) {}
