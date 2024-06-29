package com.narlock.api.controller;

import com.narlock.api.model.Task;
import com.narlock.api.model.request.TaskSearchRequest;
import com.narlock.api.model.response.TasksResponse;
import com.narlock.api.service.TasksService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/tasks")
public class TasksRestController {
  @Autowired private TasksService tasksService;

  /**
   * Retrieves all tasks from the DynamoDB table.
   *
   * @return ResponseEntity containing a TasksResponse with the list of tasks and an HTTP status of
   *     OK (200).
   */
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<TasksResponse> getTasks() {
    List<Task> tasks = tasksService.getTasks();
    return ResponseEntity.ok().body(TasksResponse.builder().tasks(tasks).build());
  }

  /**
   * Retrieves a single task from DynamoDB by its taskId.
   *
   * @param taskId The ID of the task to retrieve.
   * @return ResponseEntity containing the retrieved Task and an HTTP status of OK (200).
   */
  @GetMapping("/{taskId}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Task> getTaskById(@PathVariable(value = "taskId") String taskId) {
    Task createdTask = tasksService.getTaskById(taskId);
    return ResponseEntity.ok().body(createdTask);
  }

  /**
   * Searches for tasks in DynamoDB based on the provided search criteria.
   *
   * @param searchRequest The TaskSearchRequest object containing search criteria and conditions.
   * @return ResponseEntity containing a TasksResponse with the list of matching tasks and an HTTP
   *     status of OK (200).
   */
  @PostMapping("/search")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<TasksResponse> searchTasks(@RequestBody TaskSearchRequest searchRequest) {
    List<Task> tasks = tasksService.searchTasks(searchRequest);
    return ResponseEntity.ok().body(TasksResponse.builder().tasks(tasks).build());
  }

  /**
   * Creates a new task in DynamoDB.
   *
   * @param taskBody The Task object containing details of the task to create.
   * @return ResponseEntity containing the created Task and an HTTP status of CREATED (201).
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<Task> createTask(@RequestBody Task taskBody) {
    Task createdTask = tasksService.createTask(taskBody);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
  }

  /**
   * Overwrites an existing task by taskId and full task body. The task body must contain the same
   * dueDate as the existing dueDate on the task. This endpoint will result in failure under the
   * condition that the task does not exist or the dueDate does not match the existing dueDate. This
   * is due to the nature of DynamoDB. Since our table exists more or less relationally, this is the
   * constraint that we will put on this operation.
   *
   * @param taskId The ID of the task to overwrite.
   * @param taskBody The Task object containing details of the task to overwrite.
   * @return ResponseEntity containing the overwritten Task and an HTTP status of OK (200).
   */
  @PutMapping("/{taskId}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Task> overwriteTask(
      @PathVariable(value = "taskId") String taskId, @RequestBody Task taskBody) {
    Task updatedTask = tasksService.overwriteTask(taskId, taskBody);
    return ResponseEntity.ok().body(updatedTask);
  }

  /**
   * Deletes a task matching the taskId provided.
   *
   * @param taskId The ID of the task to delete.
   */
  @DeleteMapping("/{taskId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTask(@PathVariable(value = "taskId") String taskId) {
    tasksService.deleteTask(taskId);
  }
}
