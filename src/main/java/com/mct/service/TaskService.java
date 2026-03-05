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
        ensureUserExists(userId);
        List<Task> tasks;
        if (status != null) {
            tasks = Task.list("user.id = ?1 and status = ?2", userId, status);
        } else {
            tasks = Task.list("user.id", userId);
        }
        return tasks.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public TaskResponse findById(UUID userId, UUID taskId) {
        ensureUserExists(userId);
        Task task = findTask(userId, taskId);
        return toResponse(task);
    }

    @Transactional
    public TaskResponse create(UUID userId, CreateTaskRequest dto) {
        User user = User.findById(userId);
        if (user == null) {
            throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
        }

        // Validação de negócio: não permitir tarefa duplicada para o mesmo usuário
        if (Task.count("user.id = ?1 and title = ?2", userId, dto.title()) > 0) {
            throw new WebApplicationException("Já existe uma tarefa com este título para este usuário", Response.Status.CONFLICT);
        }

        Task task = new Task();
        task.title = dto.title();
        task.description = dto.description();
        task.status = dto.status() != null ? dto.status() : TaskStatus.OPEN;
        task.dueDate = dto.dueDate();
        task.user = user;

        task.persist();
        return toResponse(task);
    }

    @Transactional
    public TaskResponse update(UUID userId, UUID taskId, UpdateTaskRequest dto) {
        ensureUserExists(userId);
        Task task = findTask(userId, taskId);

        // Se o título mudou, validar duplicidade
        if (!task.title.equals(dto.title())) {
            if (Task.count("user.id = ?1 and title = ?2 and id != ?3", userId, dto.title(), taskId) > 0) {
                throw new WebApplicationException("Já existe uma tarefa com este título para este usuário", Response.Status.CONFLICT);
            }
        }

        task.title = dto.title();
        task.description = dto.description();
        task.status = dto.status();
        task.dueDate = dto.dueDate();

        return toResponse(task);
    }

    @Transactional
    public void delete(UUID userId, UUID taskId) {
        ensureUserExists(userId);
        Task task = findTask(userId, taskId);
        task.delete();
    }

    private void ensureUserExists(UUID userId) {
        if (User.count("id", userId) == 0) {
            throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
        }
    }

    private Task findTask(UUID userId, UUID taskId) {
        Task task = Task.find("id = ?1 and user.id = ?2", taskId, userId).firstResult();
        if (task == null) {
            throw new WebApplicationException("Tarefa não encontrada", Response.Status.NOT_FOUND);
        }
        return task;
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.id,
                task.user.id,
                task.title,
                task.description,
                task.status,
                task.dueDate,
                task.createdAt
        );
    }
}
