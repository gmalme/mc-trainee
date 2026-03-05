package com.mct.resource;

import com.mct.dto.CreateUserRequest;
import com.mct.dto.UserResponse;
import com.mct.service.UserService;
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

/**
 * Gestão de usuários do sistema. Apenas administradores podem acessar.
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Users", description = "Gestão de usuários")
public class UserResource {

    @Inject
    UserService userService;

    @GET
    @Operation(summary = "Lista todos os usuários")
    public List<UserResponse> getAll() {
        return userService.listAll();
    }

    @GET
    @Path("/{userId}")
    @Operation(summary = "Obtém detalhes de um usuário")
    @APIResponse(responseCode = "200", description = "Usuário encontrado")
    @APIResponse(responseCode = "404", description = "Usuário não encontrado")
    public UserResponse getById(@PathParam("userId") UUID userId) {
        return userService.findById(userId);
    }

    @POST
    @Operation(summary = "Cria um novo usuário")
    @APIResponse(responseCode = "201", description = "Usuário criado")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "409", description = "Username ou Email já existe")
    public Response create(@Valid CreateUserRequest dto) {
        UserResponse response = userService.create(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}
