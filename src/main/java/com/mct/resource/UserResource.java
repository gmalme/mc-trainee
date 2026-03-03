package com.mct.resource;

import com.mct.domain.model.User;
import com.mct.dto.UserCreateDTO;
import com.mct.dto.UserDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Gestão de usuários do sistema. Apenas administradores podem acessar.
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class UserResource {

    @GET
    public List<UserDTO> getAll() {
        return User.<User>listAll().stream()
                .map(u -> new UserDTO(u.id, u.username, u.role))
                .collect(Collectors.toList());
    }

    @POST
    @Transactional
    public Response create(@Valid UserCreateDTO dto) {
        if (User.find("username", dto.username()).count() > 0) {
            throw new WebApplicationException("Usuário já existe", Response.Status.CONFLICT);
        }
        User user = new User();
        user.username = dto.username();
        user.passwordHash = dto.password(); // Hash simplificado para o treinamento
        user.role = dto.role();
        user.persist();
        return Response.status(Response.Status.CREATED).entity(new UserDTO(user.id, user.username, user.role)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public UserDTO update(@PathParam("id") UUID id, @Valid UserCreateDTO dto) {
        User user = User.findById(id);
        if (user == null) {
            throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
        }
        user.username = dto.username();
        user.passwordHash = dto.password();
        user.role = dto.role();
        return new UserDTO(user.id, user.username, user.role);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") UUID id) {
        User user = User.findById(id);
        if (user == null) {
            throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
        }
        user.delete();
    }
}
