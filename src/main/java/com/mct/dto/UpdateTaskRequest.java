package com.mct.dto;

import com.mct.domain.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public record UpdateTaskRequest(
    @NotBlank(message = "Título não pode ficar vazio")
    String title,
    
    String description,
    
    TaskStatus status,
    
    Instant dueDate
) {}
