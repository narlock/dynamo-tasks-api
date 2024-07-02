package com.narlock.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.narlock.api.model.Task;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class TasksMapperTests {
  TasksMapper tasksMapper = new TasksMapper();

  @Test
  public void testConvertMapToTask() {
    // Given
    Map<String, AttributeValue> item = new HashMap<>();
    item.put("PK", AttributeValue.builder().s("task1").build());
    item.put("SK", AttributeValue.builder().s("2024-07-02").build());
    item.put("title", AttributeValue.builder().s("Task Title").build());
    item.put("description", AttributeValue.builder().s("Task Description").build());
    item.put("status", AttributeValue.builder().s("IN_PROGRESS").build());
    item.put("priority", AttributeValue.builder().n("1").build());
    item.put("tags", AttributeValue.builder().ss("tag1", "tag2").build());
    item.put("userId", AttributeValue.builder().s("user1").build());

    // When
    Task result = tasksMapper.convertMapToTask(item);

    // Then
    assertNotNull(result);
    assertEquals("task1", result.getId());
    assertEquals("2024-07-02", result.getDueDate());
    assertEquals("Task Title", result.getTitle());
    assertEquals("Task Description", result.getDescription());
    assertEquals("IN_PROGRESS", result.getStatus());
    assertEquals(1, result.getPriority());
    assertEquals(Arrays.asList("tag1", "tag2"), result.getTags());
    assertEquals("user1", result.getUserId());
  }
}
