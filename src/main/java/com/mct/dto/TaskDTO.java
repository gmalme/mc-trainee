package com.mct.dto;

import com.mct.domain.enums.TaskStatus;
import java.time.Instant;
import java.util.UUID;

/**
 * Representação de uma tarefa nas respostas da API.
 */
public record TaskDTO(
    UUID id,
    String title,
    String description,
    TaskStatus status,
    Instant createdAt,
    UserDTO assignedTo
) {}
