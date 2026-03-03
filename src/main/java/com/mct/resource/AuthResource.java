package com.mct.resource;

import com.mct.dto.LoginRequestDTO;
import com.mct.dto.LoginResponseDTO;
import com.mct.service.AuthService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Endpoint para autenticação de usuários.
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    public LoginResponseDTO login(@Valid LoginRequestDTO loginRequest) {
        return authService.login(loginRequest);
    }
}
