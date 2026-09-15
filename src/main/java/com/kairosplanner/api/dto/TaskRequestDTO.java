package com.kairosplanner.api.dto;

import com.kairosplanner.api.model.TaskStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record TaskRequestDTO(
        @NotBlank(message = "El título es obligatorio")
        String title,

        String description,
        LocalDate deadline,

        @NotNull(message = "El estado es obligatorio")
        TaskStatus status,

        @Valid
        List<SubtaskRequestDTO> subtasks
) {
}
