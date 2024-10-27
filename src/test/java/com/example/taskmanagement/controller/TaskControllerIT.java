package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.repository.TaskRepository;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.ErrorResponse;
import org.openapitools.model.ErrorResponseErrorsInner;
import org.openapitools.model.TaskRequest;
import org.openapitools.model.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
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

        TaskResponse taskResponse1 = responseList.getFirst();
        assertEquals("Task 1", taskResponse1.getTitle());
        assertEquals("Description for Task 1", taskResponse1.getDescription());
        assertEquals(LocalDate.now().plusDays(5), taskResponse1.getDueDate());
        assertEquals(false, taskResponse1.getCompleted());

        TaskResponse taskResponse2 = responseList.getLast();
        assertEquals("Task 2", taskResponse2.getTitle());
        assertEquals("Description for Task 2", taskResponse2.getDescription());
        assertEquals(LocalDate.now().plusDays(10), taskResponse2.getDueDate());
        assertEquals(true, taskResponse2.getCompleted());
    }

    @Test
    public void givenTaskExists_whenGetTaskById_thenReturnTask() {
        Task task = new Task();
        task.setTitle("Task 1");
        task.setDescription("Description for Task 1");
        task.setDueDate(LocalDate.now().plusDays(5));
        task.setCompleted(false);

        task = taskRepository.save(task);

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
        assertEquals(false, taskResponse.getCompleted());
    }

    @Test
    public void givenTaskDoesNotExist_whenGetTaskById_thenReturnNotFound() {
        Long nonExistentTaskId = 999L;

        Response response = given()
                .when()
                .get("/tasks/{id}", nonExistentTaskId)
                .then()
                .statusCode(404)
                .extract()
                .response();

        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        assertEquals("Resource not found", errorResponse.getMessage());
        assertTrue(errorResponse.getErrors().isEmpty());
    }

    // Tests for Creating a New Task

    @Test
    public void givenValidTaskRequest_whenCreateTask_thenReturnCreatedTask() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setTitle("New Task");
        taskRequest.setDescription("This is a new task description");
        taskRequest.setDueDate(LocalDate.now().plusDays(7));

        Response response = given()
                .contentType(ContentType.JSON)
                .body(taskRequest)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201)
                .extract()
                .response();

        TaskResponse taskResponse = response.as(TaskResponse.class);

        assertNotNull(taskResponse.getId());
        assertEquals("New Task", taskResponse.getTitle());
        assertEquals("This is a new task description", taskResponse.getDescription());
        assertEquals(LocalDate.now().plusDays(7), taskResponse.getDueDate());
        assertEquals(false, taskResponse.getCompleted());
    }

    @Test
    public void givenInvalidTaskRequest_whenCreateTask_thenReturnBadRequest() {
        TaskRequest invalidTaskRequest = new TaskRequest();
        invalidTaskRequest.setDescription("This task has no title");
        invalidTaskRequest.setDueDate(null);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(invalidTaskRequest)
                .when()
                .post("/tasks")
                .then()
                .statusCode(400)
                .extract()
                .response();

        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        assertEquals("Validation failed", errorResponse.getMessage());

        List<ErrorResponseErrorsInner> errors = errorResponse.getErrors();

        assertEquals(1, errors.size());

        assertEquals("title", errors.get(0).getField());
        assertEquals("must not be null", errors.get(0).getMessage());
    }

    // Tests for Updating a Task

    @Test
    public void givenTaskExists_whenUpdateTask_thenReturnUpdatedTask() {
        Task task = new Task();
        task.setTitle("Original Task");
        task.setDescription("Original description");
        task.setDueDate(LocalDate.now().plusDays(5));
        task.setCompleted(false);

        task = taskRepository.save(task);

        TaskRequest updatedTaskRequest = new TaskRequest();
        updatedTaskRequest.setTitle("Updated Task");
        updatedTaskRequest.setDescription("Updated description");
        updatedTaskRequest.setDueDate(LocalDate.now().plusDays(10));

        Response response = given()
                .contentType(ContentType.JSON)
                .body(updatedTaskRequest)
                .when()
                .put("/tasks/{id}", task.getId())
                .then()
                .statusCode(200)
                .extract()
                .response();

        TaskResponse taskResponse = response.as(TaskResponse.class);

        assertEquals(task.getId(), taskResponse.getId());

        assertEquals("Updated Task", taskResponse.getTitle());
        assertEquals("Updated description", taskResponse.getDescription());
        assertEquals(LocalDate.now().plusDays(10), taskResponse.getDueDate());
        assertEquals(false, taskResponse.getCompleted());
    }

    @Test
    public void givenTaskDoesNotExist_whenUpdateTask_thenReturnNotFound() {
        Long nonExistentTaskId = 999L;

        TaskRequest updatedTaskRequest = new TaskRequest();
        updatedTaskRequest.setTitle("Updated Task");
        updatedTaskRequest.setDescription("Updated description");
        updatedTaskRequest.setDueDate(LocalDate.now().plusDays(10));

        Response response = given()
                .contentType(ContentType.JSON)
                .body(updatedTaskRequest)
                .when()
                .put("/tasks/{id}", nonExistentTaskId)
                .then()
                .statusCode(404)
                .extract()
                .response();

        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        assertEquals("Resource not found", errorResponse.getMessage());
        assertTrue(errorResponse.getErrors().isEmpty());
    }

    @Test
    public void givenInvalidTaskUpdateRequest_whenUpdateTask_thenReturnBadRequest() {
        Task task = new Task();
        task.setTitle("Original Task");
        task.setDescription("Original description");
        task.setDueDate(LocalDate.now().plusDays(5));
        task.setCompleted(false);

        task = taskRepository.save(task);

        TaskRequest invalidTaskRequest = new TaskRequest();
        invalidTaskRequest.setDescription("Updated description");
        invalidTaskRequest.setDueDate(LocalDate.now().minusDays(1));

        Response response = given()
                .contentType(ContentType.JSON)
                .body(invalidTaskRequest)
                .when()
                .put("/tasks/{id}", task.getId())
                .then()
                .statusCode(400)
                .extract()
                .response();

        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        assertEquals("Validation failed", errorResponse.getMessage());

        List<ErrorResponseErrorsInner> errors = errorResponse.getErrors();

        assertFalse(errors.isEmpty());
        assertEquals("title", errors.get(0).getField());
        assertEquals("must not be null", errors.get(0).getMessage());
    }

    // Tests for Deleting a Task

    @Test
    public void givenTaskExists_whenDeleteTask_thenReturnNoContent() {
        Task task = new Task();
        task.setTitle("Task to be deleted");
        task.setDescription("Description for the task to be deleted");
        task.setDueDate(LocalDate.now().plusDays(5));
        task.setCompleted(false);

        task = taskRepository.save(task);

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/tasks/{id}", task.getId())
                .then()
                .statusCode(204);

        boolean taskExists = taskRepository.existsById(task.getId());
        assertFalse(taskExists, "Task should be deleted from the repository");
    }

    @Test
    public void givenTaskDoesNotExist_whenDeleteTask_thenReturnNotFound() {
        Long nonExistentTaskId = 999L;

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/tasks/{id}", nonExistentTaskId)
                .then()
                .statusCode(404)
                .extract()
                .response();

        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        assertEquals("Resource not found", errorResponse.getMessage());

        assertTrue(errorResponse.getErrors().isEmpty());
    }

    // Tests for Marking a Task as Completed

    @Test
    public void givenTaskExists_whenMarkTaskAsCompleted_thenReturnCompletedTask() {
        Task task = new Task();
        task.setTitle("Task to be marked as completed");
        task.setDescription("Description for the task to be completed");
        task.setDueDate(LocalDate.now().plusDays(5));
        task.setCompleted(false);

        task = taskRepository.save(task);

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .patch("/tasks/{id}/complete", task.getId())
                .then()
                .statusCode(200)
                .extract()
                .response();

        TaskResponse taskResponse = response.as(TaskResponse.class);

        assertEquals(task.getId(), taskResponse.getId());

        assertTrue(taskResponse.getCompleted());
        assertEquals("Task to be marked as completed", taskResponse.getTitle());
        assertEquals("Description for the task to be completed", taskResponse.getDescription());
        assertEquals(LocalDate.now().plusDays(5), taskResponse.getDueDate());
    }

    @Test
    public void givenTaskDoesNotExist_whenMarkTaskAsCompleted_thenReturnNotFound() {
        Long nonExistentTaskId = 999L;

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .patch("/tasks/{id}/complete", nonExistentTaskId)
                .then()
                .statusCode(404)
                .extract()
                .response();

        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        assertEquals("Resource not found", errorResponse.getMessage());
        assertTrue(errorResponse.getErrors().isEmpty());
    }
}
