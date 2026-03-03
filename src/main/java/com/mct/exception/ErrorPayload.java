package com.mct.exception;

import java.time.Instant;
import java.util.List;

/**
 * Payload padrão para respostas de erro da API.
 */
public record ErrorPayload(
    Instant timestamp,
    String code,
    String message,
    List<String> details,
    String path
) {}
