package com.kairosplanner.api.dto;

import com.kairosplanner.api.model.TaskStatus;

import java.time.LocalDate;
import java.util.List;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        LocalDate deadline,
        TaskStatus status,
        boolean completed,
        boolean canceled,
        List<SubtaskDTO> subtasks
) {
}
