package com.example.taskmanagement.mapper;

import com.example.taskmanagement.model.Task;
import org.openapitools.model.TaskRequest;
import org.openapitools.model.TaskResponse;

public class TaskMapper {

    public static Task mapFromTaskRequest(TaskRequest taskRequest) {
        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());

        return task;
    }

    public static TaskResponse mapToTaskResponse(Task task) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(task.getId());
        taskResponse.setTitle(task.getTitle());
        taskResponse.setDescription(task.getDescription());
        taskResponse.setCompleted(task.isCompleted());
        taskResponse.setDueDate(task.getDueDate());

        return taskResponse;
    }
}
