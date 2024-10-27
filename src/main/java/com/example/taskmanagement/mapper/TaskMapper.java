package com.example.taskmanagement.mapper;

import com.example.taskmanagement.model.Task;
import org.openapitools.model.TaskRequest;
import org.openapitools.model.TaskResponse;

import java.util.Optional;

public class TaskMapper {

    public static Task mapFromTaskRequest(TaskRequest taskRequest) {
        Task task = new Task();
        Optional.ofNullable(taskRequest.getTitle()).ifPresent(task::setTitle);
        Optional.ofNullable(taskRequest.getDescription()).ifPresent(task::setDescription);
        Optional.ofNullable(taskRequest.getDueDate()).ifPresent(task::setDueDate);

        return task;
    }

    public static TaskResponse mapToTaskResponse(Task task) {
        TaskResponse taskResponse = new TaskResponse();
        Optional.ofNullable(task.getId()).ifPresent(taskResponse::setId);
        Optional.ofNullable(task.getTitle()).ifPresent(taskResponse::setTitle);
        Optional.ofNullable(task.getDescription()).ifPresent(taskResponse::setDescription);
        Optional.of(task.isCompleted()).ifPresent(taskResponse::setCompleted);
        Optional.ofNullable(task.getDueDate()).ifPresent(taskResponse::setDueDate);

        return taskResponse;
    }
}
