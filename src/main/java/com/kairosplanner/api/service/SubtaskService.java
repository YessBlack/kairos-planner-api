package com.kairosplanner.api.service;

import com.kairosplanner.api.dto.SubtaskDTO;
import com.kairosplanner.api.dto.SubtaskRequestDTO;
import com.kairosplanner.api.exception.ResourceNotFoundException;
import com.kairosplanner.api.model.Subtask;
import com.kairosplanner.api.model.Task;
import com.kairosplanner.api.repository.SubtaskRepository;
import com.kairosplanner.api.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubtaskService {
    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;

    public SubtaskService(SubtaskRepository subtaskRepository, TaskRepository taskRepository) {
        this.subtaskRepository = subtaskRepository;
        this.taskRepository = taskRepository;
    }

    public List<SubtaskDTO> getSubtasksByTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task not found with id: " + taskId);
        }

        return subtaskRepository.findByTaskId(taskId)
                .stream()
                .map(s -> new SubtaskDTO(s.getId(), s.getText(), s.isDone()))
                .toList();
    }

    public SubtaskDTO createSubtask(Long taskId, SubtaskRequestDTO data) {
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        Subtask subtask = new Subtask();
        subtask.setText(data.text());
        subtask.setDone(data.done());
        subtask.setTask(task);

        Subtask createdSubtask = subtaskRepository.save(subtask);
        return new SubtaskDTO(createdSubtask.getId(), createdSubtask.getText(), createdSubtask.isDone());
    }

    public SubtaskDTO updateSubtask(Long taskId, Long subtaskId, SubtaskRequestDTO data) {
        Subtask subtask = findSubtaskInTask(taskId, subtaskId);

        subtask.setText(data.text());
        subtask.setDone(data.done());

        Subtask updatedSubtask = subtaskRepository.save(subtask);
        return new SubtaskDTO(updatedSubtask.getId(), updatedSubtask.getText(), updatedSubtask.isDone());
    }

    public void deleteSubtask(Long taskId, Long subtaskId) {
        Subtask subtask = findSubtaskInTask(taskId, subtaskId);
        subtaskRepository.delete(subtask);
    }

    private Subtask findSubtaskInTask(Long taskId, Long subtaskId) {
        Subtask subtask = subtaskRepository
                .findById(subtaskId)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found with id: " + subtaskId));

        if (!subtask.getTask().getId().equals(taskId)) {
            throw new ResourceNotFoundException("Subtask with id: " + subtaskId + " does not belong to task with id: " + taskId);
        }

        return subtask;
    }
}