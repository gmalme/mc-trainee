package com.mct.dto;

import com.mct.domain.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 * Dados para criação ou atualização de uma tarefa.
 */
public record TaskCreateDTO(
    @NotBlank @Size(min = 3, max = 120) String title,
    String description,
    TaskStatus status,
    UUID assignedToId
) {}
