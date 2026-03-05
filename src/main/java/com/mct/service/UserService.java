package com.mct.service;

import com.mct.domain.model.User;
import com.mct.dto.CreateUserRequest;
import com.mct.dto.UserResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {

    public List<UserResponse> listAll() {
        return User.<User>listAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse findById(UUID id) {
        User user = User.findById(id);
        if (user == null) {
            throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
        }
        return toResponse(user);
    }

    @Transactional
    public UserResponse create(CreateUserRequest dto) {
        if (User.find("username", dto.username()).count() > 0) {
            throw new WebApplicationException("Username já existe", Response.Status.CONFLICT);
        }
        if (User.find("email", dto.email()).count() > 0) {
            throw new WebApplicationException("Email já cadastrado", Response.Status.CONFLICT);
        }

        User user = new User();
        user.name = dto.name();
        user.email = dto.email();
        user.username = dto.username();
        user.passwordHash = dto.password(); // Em produção: hash da senha!
        user.role = dto.role() != null ? dto.role() : com.mct.domain.enums.Role.USER;
        
        user.persist();
        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.id,
                user.name,
                user.email,
                user.username,
                user.role,
                user.createdAt
        );
    }
}
