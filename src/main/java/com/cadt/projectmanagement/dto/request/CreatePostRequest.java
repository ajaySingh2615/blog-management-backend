package com.cadt.projectmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePostRequest(
        @NotBlank
        @Size(max = 160)
        String title,
        @NotBlank
        String content,
        @NotBlank
        String status) {
}
