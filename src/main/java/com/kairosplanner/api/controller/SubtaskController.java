package com.kairosplanner.api.controller;

import com.kairosplanner.api.dto.SubtaskDTO;
import com.kairosplanner.api.dto.SubtaskRequestDTO;
import com.kairosplanner.api.service.SubtaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/subtasks")
public class SubtaskController {
    private final SubtaskService subtaskService;

    public SubtaskController(SubtaskService subtaskService) {
        this.subtaskService = subtaskService;
    }

    @GetMapping
    public ResponseEntity<List<SubtaskDTO>> getSubtasksByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(subtaskService.getSubtasksByTask(taskId));
    }

    @PostMapping
    public ResponseEntity<SubtaskDTO> createSubtask(@PathVariable Long taskId, @Valid @RequestBody SubtaskRequestDTO data) {
        SubtaskDTO createdSubtask = subtaskService.createSubtask(taskId, data);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubtask);
    }

    @PutMapping("/{subtaskId}")
    public ResponseEntity<SubtaskDTO> updateSubtask(@PathVariable Long taskId, @PathVariable Long subtaskId, @Valid @RequestBody SubtaskRequestDTO data) {
        return ResponseEntity.ok(subtaskService.updateSubtask(taskId, subtaskId, data));
    }

    @DeleteMapping("/{subtaskId}")
    public ResponseEntity<Void> deleteSubtask(@PathVariable Long taskId, @PathVariable Long subtaskId) {
        subtaskService.deleteSubtask(taskId, subtaskId);
        return ResponseEntity.noContent().build();
    }
}