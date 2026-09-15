package com.kairosplanner.api.dto;

import jakarta.validation.constraints.NotBlank;

public record SubtaskRequestDTO(
        @NotBlank(message = "El texto de la subtarea no puede estar vacío")
        String text,

        boolean done
) {
}
