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
        String code = "INTERNAL_ERROR";
        String message = exception.getMessage();
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        List<String> details = null;

        if (exception instanceof WebApplicationException webAppEx) {
            status = Response.Status.fromStatusCode(webAppEx.getResponse().getStatus());
            code = switch (status) {
                case NOT_FOUND -> "NOT_FOUND";
                case UNAUTHORIZED -> "UNAUTHORIZED";
                case FORBIDDEN -> "FORBIDDEN";
                case CONFLICT -> "CONFLICT";
                default -> "ERROR";
            };
        } else if (exception instanceof ConstraintViolationException cve) {
            status = Response.Status.BAD_REQUEST;
            code = "VALIDATION_ERROR";
            message = "Erro de validação nos campos.";
            details = cve.getConstraintViolations().stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.toList());
        }

        ErrorPayload payload = new ErrorPayload(
                Instant.now(),
                code,
                message,
                details,
                uriInfo.getPath()
        );

        return Response.status(status).entity(payload).build();
    }
}
