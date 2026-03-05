package com.mct.resource;

import com.mct.domain.enums.TaskStatus;
import com.mct.dto.CreateTaskRequest;
import com.mct.dto.TaskResponse;
import com.mct.dto.UpdateTaskRequest;
import com.mct.service.TaskService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;

@Path("/users/{userId}/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Tasks", description = "Gestão de tarefas vinculadas a usuários")
public class TaskResource {

    @Inject
    TaskService taskService;

    @GET
    @Operation(summary = "Lista tarefas de um usuário")
    public List<TaskResponse> list(@PathParam("userId") UUID userId, @QueryParam("status") TaskStatus status) {
        return taskService.listByUser(userId, status);
    }

    @GET
    @Path("/{taskId}")
    @Operation(summary = "Obtém detalhes de uma tarefa")
    @APIResponse(responseCode = "200", description = "Tarefa encontrada")
    @APIResponse(responseCode = "404", description = "Usuário ou Tarefa não encontrada")
    public TaskResponse getById(@PathParam("userId") UUID userId, @PathParam("taskId") UUID taskId) {
        return taskService.findById(userId, taskId);
    }

    @POST
    @Operation(summary = "Cria uma tarefa para o usuário")
    @APIResponse(responseCode = "201", description = "Tarefa criada")
    @APIResponse(responseCode = "404", description = "Usuário não encontrado")
    @APIResponse(responseCode = "409", description = "Tarefa duplicada")
    public Response create(@PathParam("userId") UUID userId, @Valid CreateTaskRequest dto) {
        TaskResponse response = taskService.create(userId, dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{taskId}")
    @Operation(summary = "Atualiza uma tarefa")
    @APIResponse(responseCode = "200", description = "Tarefa atualizada")
    @APIResponse(responseCode = "404", description = "Usuário ou Tarefa não encontrada")
    @APIResponse(responseCode = "409", description = "Conflito ao atualizar título")
    public TaskResponse update(@PathParam("userId") UUID userId, @PathParam("taskId") UUID taskId, @Valid UpdateTaskRequest dto) {
        return taskService.update(userId, taskId, dto);
    }

    @DELETE
    @Path("/{taskId}")
    @Operation(summary = "Remove uma tarefa")
    @APIResponse(responseCode = "204", description = "Tarefa removida")
    @APIResponse(responseCode = "404", description = "Usuário ou Tarefa não encontrada")
    public Response delete(@PathParam("userId") UUID userId, @PathParam("taskId") UUID taskId) {
        taskService.delete(userId, taskId);
        return Response.noContent().build();
    }
}
