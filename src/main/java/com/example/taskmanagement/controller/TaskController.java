package com.example.taskmanagement.controller;

import com.example.taskmanagement.mapper.TaskMapper;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.service.TaskService;
import org.openapitools.api.TasksApi;
import org.openapitools.model.TaskRequest;
import org.openapitools.model.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskController implements TasksApi {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public ResponseEntity<TaskResponse> createTask(TaskRequest taskRequest) {
        Task task = TaskMapper.mapFromTaskRequest(taskRequest);
        TaskResponse taskResponse = TaskMapper.mapToTaskResponse(taskService.createTask(task));

        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);
    }

    @Override
    public ResponseEntity<Void> deleteTask(Long id) {
        taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> taskResponseList = taskService
                .getAllTasks()
                .stream()
                .map(TaskMapper::mapToTaskResponse)
                .toList();

        return ResponseEntity.ok(taskResponseList);
    }

    @Override
    public ResponseEntity<TaskResponse> getTaskById(Long id) {
        TaskResponse taskResponse = TaskMapper.mapToTaskResponse(taskService.getTaskById(id));

        return ResponseEntity.ok(taskResponse);
    }

    @Override
    public ResponseEntity<TaskResponse> markTaskAsCompleted(Long id) {
        TaskResponse taskResponse = TaskMapper.mapToTaskResponse(taskService.markTaskAsCompleted(id));

        return ResponseEntity.ok(taskResponse);
    }

    @Override
    public ResponseEntity<TaskResponse> updateTask(Long id, TaskRequest taskRequest) {
        Task task = TaskMapper.mapFromTaskRequest(taskRequest);
        TaskResponse taskResponse = TaskMapper.mapToTaskResponse(taskService.updateTask(id, task));

        return ResponseEntity.ok(taskResponse);
    }
}
