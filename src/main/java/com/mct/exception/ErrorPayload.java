package com.mct.exception;

import java.time.Instant;
import java.util.List;

/**
 * Payload padrão para respostas de erro da API.
 */
public record ErrorPayload(
    int status,
    String error,
    String message,
    String path,
    Instant timestamp
) {}
