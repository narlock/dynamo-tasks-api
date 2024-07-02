package com.narlock.api.service;

import static com.narlock.api.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.narlock.api.mapper.TasksMapper;
import com.narlock.api.model.Task;
import java.util.*;

import com.narlock.api.model.exception.ItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

public class TasksServiceTests {

  @Mock private DynamoDbClient dynamoDbClient;

  @Mock private TasksMapper tasksMapper;

  @InjectMocks private TasksService tasksService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetTasks_200() {
    // Given
    ScanResponse scanResponse = ScanResponse.builder().items(DYNAMO_SAMPLE_TASKS).build();
    when(dynamoDbClient.scan(any(ScanRequest.class))).thenReturn(scanResponse);

    when(tasksMapper.convertMapToTask(DYNAMO_MAP_SAMPLE_TASK1)).thenReturn(SAMPLE_TASK1);
    when(tasksMapper.convertMapToTask(DYNAMO_MAP_SAMPLE_TASK2)).thenReturn(SAMPLE_TASK2);

    // When
    List<Task> tasks = tasksService.getTasks();

    // Then
    assertEquals(2, tasks.size());
    assertEquals("1", tasks.get(0).getId());
    assertEquals("2", tasks.get(1).getId());
    assertEquals("2024-07-05", tasks.get(0).getDueDate());
    assertEquals("2024-07-03", tasks.get(1).getDueDate());
    assertEquals("Complete project documentation", tasks.get(0).getTitle());
    assertEquals("Grocery shopping", tasks.get(1).getTitle());
    assertEquals(
        "Finalize and submit the project documentation by the end of the week.",
        tasks.get(0).getDescription());
    assertEquals(
        "Buy groceries for the week, including fruits, vegetables, and dairy products.",
        tasks.get(1).getDescription());
    assertEquals("pending", tasks.get(0).getStatus());
    assertEquals("in-progress", tasks.get(1).getStatus());
    assertEquals(2, tasks.get(0).getPriority());
    assertEquals(1, tasks.get(1).getPriority());
    assertEquals(Arrays.asList("work", "documentation"), tasks.get(0).getTags());
    assertEquals(Arrays.asList("personal", "shopping"), tasks.get(1).getTags());
    assertEquals("user-123", tasks.get(0).getUserId());
    assertEquals("user-456", tasks.get(1).getUserId());
  }

  @Test
  public void testGetTaskById_200() {
    // Given
    ScanResponse scanResponse =
        ScanResponse.builder().items(List.of(DYNAMO_MAP_SAMPLE_TASK1)).build();
    when(dynamoDbClient.scan(any(ScanRequest.class))).thenReturn(scanResponse);
    when(tasksMapper.convertMapToTask(DYNAMO_MAP_SAMPLE_TASK1)).thenReturn(SAMPLE_TASK1);

    // When
    Task task = tasksService.getTaskById(TASK_ID);

    // Then
    assertNotNull(task);
    assertEquals("1", task.getId());
    assertEquals("2024-07-05", task.getDueDate());
    assertEquals("Complete project documentation", task.getTitle());
    assertEquals(
        "Finalize and submit the project documentation by the end of the week.",
        task.getDescription());
    assertEquals("pending", task.getStatus());
    assertEquals(2, task.getPriority());
    assertEquals(Arrays.asList("work", "documentation"), task.getTags());
    assertEquals("user-123", task.getUserId());
  }

  @Test
  public void testGetTaskById_404() {
    // Given
    ScanResponse scanResponse =
            ScanResponse.builder().items(Collections.emptyList()).build();
    when(dynamoDbClient.scan(any(ScanRequest.class))).thenReturn(scanResponse);

    // When
    Executable executable = () -> tasksService.getTaskById(TASK_ID);

    // Then
    ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class, executable);
    assertEquals("Task " + TASK_ID + " was not found", thrown.getMessage());
  }
}
