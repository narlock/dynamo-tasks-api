package com.narlock.api.controller;

import com.narlock.api.model.Task;
import com.narlock.api.model.request.TaskSearchRequest;
import com.narlock.api.model.response.TasksResponse;
import com.narlock.api.service.TasksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tasks")
public class TasksRestController {
    @Autowired
    private TasksService tasksService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TasksResponse> getTasks() {
        List<Task> tasks = tasksService.getTasks();
        return ResponseEntity.ok().body(TasksResponse.builder().tasks(tasks).build());
    }

    @GetMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Task> getTaskById(@PathVariable(value = "taskId") String taskId) {
        Task createdTask = tasksService.getTaskById(taskId);
        return ResponseEntity.ok().body(createdTask);
    }

    @PostMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TasksResponse> searchTasks(@RequestBody TaskSearchRequest searchRequest) {
        List<Task> tasks = tasksService.searchTasks(searchRequest);
        return ResponseEntity.ok().body(TasksResponse.builder().tasks(tasks).build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Task> createTask(@RequestBody Task taskBody) {
        Task createdTask = tasksService.createTask(taskBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }
}
