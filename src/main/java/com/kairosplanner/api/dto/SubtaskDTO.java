package com.kairosplanner.api.dto;

public record SubtaskDTO(
        Long id,
        String text,
        boolean done
) {
}
