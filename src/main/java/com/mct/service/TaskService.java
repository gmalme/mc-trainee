package com.mct.service;

import com.mct.domain.enums.TaskStatus;
import com.mct.domain.model.Task;
import com.mct.domain.model.User;
import com.mct.dto.CreateTaskRequest;
import com.mct.dto.TaskResponse;
import com.mct.dto.UpdateTaskRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class TaskService {

    public List<TaskResponse> listByUser(UUID userId, TaskStatus status) {
        // TODO
    }

    public TaskResponse findById(UUID userId, UUID taskId) {]
        // TODO
    }

    @Transactional
    public TaskResponse create(UUID userId, CreateTaskRequest dto) {
        // TODO
    }

    @Transactional
    public TaskResponse update(UUID userId, UUID taskId, UpdateTaskRequest dto) {
        // TODO
    }

    @Transactional
    public void delete(UUID userId, UUID taskId) {
        // TODO
    }

    private void ensureUserExists(UUID userId) {
        // TODO
    }

    private Task findTask(UUID userId, UUID taskId) {
        // TODO
    }

    private TaskResponse toResponse(Task task) {
        // TODO
    }
}
