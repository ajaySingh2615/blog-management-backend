package com.cadt.projectmanagement.dto.response;

import java.time.Instant;

public record PostResponse(
        Long id,
        String title,
        String slug,
        String content,
        String status,
        Instant createdAt,
        Instant updatedAt
) {
}
