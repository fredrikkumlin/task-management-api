package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.repository.TaskRepository;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
public class TaskControllerIT {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void setup() {
        taskRepository.deleteAll();
    }

    // Tests for Retrieving Tasks

    @Test
    public void givenTasksExist_whenGetAllTasks_thenReturnTaskList() {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description for Task 1");
        task1.setDueDate(LocalDate.now().plusDays(5));
        task1.setCompleted(false);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description for Task 2");
        task2.setDueDate(LocalDate.now().plusDays(10));
        task2.setCompleted(true);

        taskRepository.saveAll(List.of(task1, task2));

        List<TaskResponse> responseList = given()
                .when()
                .get("/tasks").then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", TaskResponse.class);;

        assertEquals(2, responseList.size());

        TaskResponse taskResponse1 = responseList.get(0);
        assertEquals("Task 1", taskResponse1.getTitle());
        assertEquals("Description for Task 1", taskResponse1.getDescription());
        assertEquals(LocalDate.now().plusDays(5), taskResponse1.getDueDate());
        assertEquals(true, taskResponse1.getCompleted());

        TaskResponse taskResponse2 = responseList.get(1);
        assertEquals("Task 2", taskResponse2.getTitle());
        assertEquals("Description for Task 2", taskResponse2.getDescription());
        assertEquals(LocalDate.now().plusDays(10), taskResponse2.getDueDate());
        assertEquals(false, taskResponse2.getCompleted());
    }

    @Test
    public void givenTaskExists_whenGetTaskById_thenReturnTask() {
        Task task = new Task();
        task.setTitle("Task 1");
        task.setDescription("Description for Task 1");
        task.setDueDate(LocalDate.now().plusDays(5));
        task.setCompleted(false);

        taskRepository.save(task);

        Response response = given()
                .when()
                .get("/tasks/{id}", task.getId())
                .then()
                .statusCode(200)
                .extract()
                .response();

        TaskResponse taskResponse = response.as(TaskResponse.class);

        assertEquals(task.getId(), taskResponse.getId());
        assertEquals("Task 1", taskResponse.getTitle());
        assertEquals("Description for Task 1", taskResponse.getDescription());
        assertEquals(LocalDate.now().plusDays(5), taskResponse.getDueDate());
        assertEquals(true, taskResponse.getCompleted());
    }

    @Test
    public void givenTaskDoesNotExist_whenGetTaskById_thenReturnNotFound() {

    }

    // Tests for Creating a New Task

    @Test
    public void givenValidTaskRequest_whenCreateTask_thenReturnCreatedTask() {

    }

    @Test
    public void givenInvalidTaskRequest_whenCreateTask_thenReturnBadRequest() {

    }

    // Tests for Updating a Task

    @Test
    public void givenTaskExists_whenUpdateTask_thenReturnUpdatedTask() {

    }

    @Test
    public void givenTaskDoesNotExist_whenUpdateTask_thenReturnNotFound() {

    }

    @Test
    public void givenInvalidTaskUpdateRequest_whenUpdateTask_thenReturnBadRequest() {

    }

    // Tests for Deleting a Task

    @Test
    public void givenTaskExists_whenDeleteTask_thenReturnNoContent() {

    }

    @Test
    public void givenTaskDoesNotExist_whenDeleteTask_thenReturnNotFound() {

    }

    // Tests for Marking a Task as Completed

    @Test
    public void givenTaskExists_whenMarkTaskAsCompleted_thenReturnCompletedTask() {

    }

    @Test
    public void givenTaskDoesNotExist_whenMarkTaskAsCompleted_thenReturnNotFound() {

    }
}
