package com.cadt.projectmanagement.exception;

import java.time.Instant;

public record ErrorResponse(
        String path,
        int status,
        String error,
        String message,
        Instant timestamp
) {
}
