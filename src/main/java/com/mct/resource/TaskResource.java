package com.mct.resource;

import com.mct.domain.enums.Role;
import com.mct.domain.enums.TaskStatus;
import com.mct.domain.model.Task;
import com.mct.domain.model.User;
import com.mct.dto.TaskCreateDTO;
import com.mct.dto.TaskDTO;
import com.mct.dto.UserDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Gestão de tarefas. Regras de visibilidade aplicadas por usuário autenticado.
 */
@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN", "USER"})
public class TaskResource {

    @Inject
    JsonWebToken jwt;

    @GET
    public List<TaskDTO> getAll(@QueryParam("status") TaskStatus status) {
        UUID userId = UUID.fromString(jwt.getClaim("userId"));
        boolean isAdmin = jwt.getGroups().contains(Role.ADMIN.name());

        StringBuilder query = new StringBuilder();
        if (!isAdmin) {
            query.append("assignedTo.id = '").append(userId).append("'");
        }
        
        if (status != null) {
            if (query.length() > 0) query.append(" AND ");
            query.append("status = '").append(status).append("'");
        }

        List<Task> tasks;
        if (query.length() > 0) {
            tasks = Task.find(query.toString()).list();
        } else {
            tasks = Task.listAll();
        }

        return tasks.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @POST
    @Transactional
    public Response create(@Valid TaskCreateDTO dto) {
        UUID currentUserId = UUID.fromString(jwt.getClaim("userId"));
        boolean isAdmin = jwt.getGroups().contains(Role.ADMIN.name());

        Task task = new Task();
        task.title = dto.title();
        task.description = dto.description();
        task.status = dto.status() != null ? dto.status() : TaskStatus.TODO;

        UUID targetUserId = (isAdmin && dto.assignedToId() != null) ? dto.assignedToId() : currentUserId;
        User user = User.findById(targetUserId);
        if (user == null) {
            throw new WebApplicationException("Usuário designado não encontrado", Response.Status.NOT_FOUND);
        }
        task.assignedTo = user;
        task.persist();

        return Response.status(Response.Status.CREATED).entity(mapToDTO(task)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public TaskDTO update(@PathParam("id") UUID id, @Valid TaskCreateDTO dto) {
        Task task = findAndVerifyOwnership(id);
        task.title = dto.title();
        task.description = dto.description();
        if (dto.status() != null) task.status = dto.status();
        
        if (jwt.getGroups().contains(Role.ADMIN.name()) && dto.assignedToId() != null) {
            User newUser = User.findById(dto.assignedToId());
            if (newUser != null) task.assignedTo = newUser;
        }

        return mapToDTO(task);
    }

    @PATCH
    @Path("/{id}/status")
    @Transactional
    public TaskDTO updateStatus(@PathParam("id") UUID id, TaskCreateDTO dto) {
        Task task = findAndVerifyOwnership(id);
        if (dto.status() == null) {
             throw new WebApplicationException("Status é obrigatório", Response.Status.BAD_REQUEST);
        }
        task.status = dto.status();
        return mapToDTO(task);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") UUID id) {
        Task task = findAndVerifyOwnership(id);
        task.delete();
    }

    private Task findAndVerifyOwnership(UUID id) {
        Task task = Task.findById(id);
        if (task == null) {
            throw new WebApplicationException("Tarefa não encontrada", Response.Status.NOT_FOUND);
        }

        UUID currentUserId = UUID.fromString(jwt.getClaim("userId"));
        boolean isAdmin = jwt.getGroups().contains(Role.ADMIN.name());

        if (!isAdmin && !task.assignedTo.id.equals(currentUserId)) {
            throw new WebApplicationException("Acesso negado a esta tarefa", Response.Status.FORBIDDEN);
        }
        return task;
    }

    private TaskDTO mapToDTO(Task t) {
        return new TaskDTO(
            t.id, t.title, t.description, t.status, t.createdAt,
            new UserDTO(t.assignedTo.id, t.assignedTo.username, t.assignedTo.role)
        );
    }
}
