package com.mct.dto;

import com.mct.domain.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public record CreateTaskRequest(
    @NotBlank(message = "Título é obrigatório")
    String title,
    
    String description,
    
    TaskStatus status,
    
    Instant dueDate
) {}
