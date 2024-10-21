package com.example.taskmanagement.controller;

import com.example.taskmanagement.service.TaskService;
import org.openapitools.api.TasksApi;
import org.openapitools.model.TaskRequest;
import org.openapitools.model.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

@RestController
public class TaskController implements TasksApi {

    private final TaskService taskService;

    @Autowired
    public TaskController(
            TaskService taskService
    ) {
        this.taskService = taskService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return TasksApi.super.getRequest();
    }

    @Override
    public ResponseEntity<TaskResponse> createTask(TaskRequest taskRequest) {
        return TasksApi.super.createTask(taskRequest);
    }

    @Override
    public ResponseEntity<Void> deleteTask(Long id) {
        return TasksApi.super.deleteTask(id);
    }

    @Override
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return TasksApi.super.getAllTasks();
    }

    @Override
    public ResponseEntity<TaskResponse> getTaskById(Long id) {
        return TasksApi.super.getTaskById(id);
    }

    @Override
    public ResponseEntity<TaskResponse> markTaskAsCompleted(Long id) {
        return TasksApi.super.markTaskAsCompleted(id);
    }

    @Override
    public ResponseEntity<TaskResponse> updateTask(Long id, TaskRequest taskRequest) {
        return TasksApi.super.updateTask(id, taskRequest);
    }
}
