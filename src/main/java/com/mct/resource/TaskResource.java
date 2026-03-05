package com.mct.resource;

import com.mct.domain.enums.TaskStatus;
import com.mct.dto.CreateTaskRequest;
import com.mct.dto.TaskResponse;
import com.mct.dto.UpdateTaskRequest;
import com.mct.service.TaskService;
import jakarta.annotation.security.RolesAllowed;
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

// TODO
public class TaskResource {

    // Inject

    public List<TaskResponse> list(@PathParam("userId") UUID userId, @QueryParam("status") TaskStatus status) {
        // TODO
    }


    public TaskResponse getById(@PathParam("userId") UUID userId, @PathParam("taskId") UUID taskId) {
        // TODO
    }

    public Response create(@PathParam("userId") UUID userId, @Valid CreateTaskRequest dto) {
        // TODO
    }

    public TaskResponse update(@PathParam("userId") UUID userId, @PathParam("taskId") UUID taskId, @Valid UpdateTaskRequest dto) {
        // TODO
    }

    public Response delete(@PathParam("userId") UUID userId, @PathParam("taskId") UUID taskId) {
        // TODO
    }
}
