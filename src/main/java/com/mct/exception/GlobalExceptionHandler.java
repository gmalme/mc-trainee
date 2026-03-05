package com.mct.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapeia exceções globais para o formato de resposta de erro padrão.
 */
@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {
        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        String error = "INTERNAL_SERVER_ERROR";
        String message = exception.getMessage();

        if (exception instanceof WebApplicationException webAppEx) {
            status = webAppEx.getResponse().getStatus();
            error = Response.Status.fromStatusCode(status).name();
        } else if (exception instanceof ConstraintViolationException cve) {
            status = Response.Status.BAD_REQUEST.getStatusCode();
            error = "BAD_REQUEST";
            message = cve.getConstraintViolations().stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));
        } else if (exception.getCause() instanceof org.hibernate.exception.ConstraintViolationException hcv) {
            status = Response.Status.CONFLICT.getStatusCode();
            error = "CONFLICT";
            if (hcv.getConstraintName() != null && hcv.getConstraintName().contains("uk_task_user_title")) {
                message = "Já existe uma tarefa com este título para este usuário";
            } else {
                message = "Conflito de integridade no banco de dados";
            }
        }

        ErrorPayload payload = new ErrorPayload(
                status,
                error,
                message,
                uriInfo.getPath(),
                Instant.now()
        );

        return Response.status(status).entity(payload).build();
    }
}
