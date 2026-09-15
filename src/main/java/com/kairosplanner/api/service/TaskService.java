package com.kairosplanner.api.service;

import com.kairosplanner.api.dto.SubtaskDTO;
import com.kairosplanner.api.dto.SubtaskRequestDTO;
import com.kairosplanner.api.dto.TaskRequestDTO;
import com.kairosplanner.api.dto.TaskResponseDTO;
import com.kairosplanner.api.exception.ResourceNotFoundException;
import com.kairosplanner.api.model.Subtask;
import com.kairosplanner.api.model.Task;
import com.kairosplanner.api.model.TaskStatus;
import com.kairosplanner.api.repository.TaskRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO>getAllTasks() {
        return taskRepository
                .findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponseDTO getTaskById(Long id) {
        Task task = taskRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        return mapToResponseDTO(task);
    }

    public TaskResponseDTO createTask(TaskRequestDTO data) {
        Task task = new Task();
        task.setTitle(data.title());
        task.setDescription(data.description());
        task.setDeadline(data.deadline());
        task.setStatus(data.status());
        task.setCompleted(data.status() == TaskStatus.COMPLETED);
        task.setCanceled(data.status() == TaskStatus.CANCELLED);

        if (data.subtasks() != null) {
            data.subtasks().forEach(subDto -> {
                Subtask subtask = new Subtask();
                subtask.setText(subDto.text());
                subtask.setDone(subDto.done());
                subtask.setTask(task);
                task.getSubtasks().add(subtask);
            });
        }

        Task createdTask = taskRepository.save(task);
        return mapToResponseDTO(createdTask);
    }

    public TaskResponseDTO updateTask(Long id, TaskRequestDTO data) {
        Task task = taskRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        task.setTitle(data.title());
        task.setDescription(data.description());
        task.setDeadline(data.deadline());
        task.setStatus(data.status());
        task.setCompleted(data.status() == TaskStatus.COMPLETED);
        task.setCanceled(data.status() == TaskStatus.CANCELLED);

        task.getSubtasks().clear();
        if (data.subtasks() != null) {
            data.subtasks().forEach(subDto -> {
                Subtask subtask = new Subtask();
                subtask.setText(subDto.text());
                subtask.setDone(subDto.done());
                subtask.setTask(task);
                task.getSubtasks().add(subtask);
            });
        }

        Task updatedTask = taskRepository.save(task);
        return mapToResponseDTO(updatedTask);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        taskRepository.delete(task);
    }

    public SubtaskDTO addSubtask(Long taskId, SubtaskRequestDTO data) {
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        Subtask subtask = new Subtask();
        subtask.setText(data.text());
        subtask.setDone(data.done());
        subtask.setTask(task);

        task.getSubtasks().add(subtask);
        taskRepository.save(task);

        Subtask savedSubtask = task.getSubtasks().get(task.getSubtasks().size() - 1);
        return new SubtaskDTO(savedSubtask.getId(), savedSubtask.getText(), savedSubtask.isDone());
    }

    private TaskResponseDTO mapToResponseDTO(Task task) {
        List<SubtaskDTO> subtasks = task.getSubtasks()
                .stream()
                .map(s -> new SubtaskDTO(s.getId(), s.getText(), s.isDone()))
                .toList();

        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDeadline(),
                task.getStatus(),
                task.isCompleted(),
                task.isCanceled(),
                subtasks
        );
    }
}
