package com.narlock.api;

import com.narlock.api.model.Task;
import com.narlock.api.model.request.TaskSearchCondition;
import com.narlock.api.model.request.TaskSearchCriteria;
import com.narlock.api.model.request.TaskSearchRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class TestUtils {
  public static final String TASK_ID = "1";

  // Sample task 1
  public static Task SAMPLE_TASK1 =
      Task.builder()
          .id("1")
          .dueDate("2024-07-05")
          .title("Complete project documentation")
          .description("Finalize and submit the project documentation by the end of the week.")
          .status("pending")
          .priority(2)
          .tags(Arrays.asList("work", "documentation"))
          .userId("user-123")
          .build();

  // Sample task 1 (via Dynamo)
  public static Map<String, AttributeValue> DYNAMO_MAP_SAMPLE_TASK1 = getDynamoTask1();

  private static Map<String, AttributeValue> getDynamoTask1() {
    Map<String, AttributeValue> item1 = new HashMap<>();
    item1.put("PK", AttributeValue.builder().s("1").build());
    item1.put("SK", AttributeValue.builder().s("2024-07-05").build());
    item1.put("title", AttributeValue.builder().s("Complete project documentation").build());
    item1.put(
        "description",
        AttributeValue.builder()
            .s("Finalize and submit the project documentation by the end of the week.")
            .build());
    item1.put("status", AttributeValue.builder().s("pending").build());
    item1.put("priority", AttributeValue.builder().n("2").build());
    item1.put("tags", AttributeValue.builder().ss("work", "documentation").build());
    item1.put("userId", AttributeValue.builder().s("user-123").build());
    return item1;
  }

  // Sample task 2
  public static Task SAMPLE_TASK2 =
      Task.builder()
          .id("2")
          .dueDate("2024-07-03")
          .title("Grocery shopping")
          .description(
              "Buy groceries for the week, including fruits, vegetables, and dairy products.")
          .status("in-progress")
          .priority(1)
          .tags(Arrays.asList("personal", "shopping"))
          .userId("user-456")
          .build();

  // Sample task 2 (via Dynamo)
  public static Map<String, AttributeValue> DYNAMO_MAP_SAMPLE_TASK2 = getDynamoTask2();

  private static Map<String, AttributeValue> getDynamoTask2() {
    Map<String, AttributeValue> item2 = new HashMap<>();
    item2.put("PK", AttributeValue.builder().s("2").build());
    item2.put("SK", AttributeValue.builder().s("2024-07-03").build());
    item2.put("title", AttributeValue.builder().s("Grocery shopping").build());
    item2.put(
        "description",
        AttributeValue.builder()
            .s("Buy groceries for the week, including fruits, vegetables, and dairy products.")
            .build());
    item2.put("status", AttributeValue.builder().s("in-progress").build());
    item2.put("priority", AttributeValue.builder().n("1").build());
    item2.put("tags", AttributeValue.builder().ss("personal", "shopping").build());
    item2.put("userId", AttributeValue.builder().s("user-456").build());
    return item2;
  }

  // List of sample tasks
  public static List<Task> SAMPLE_TASKS = List.of(SAMPLE_TASK1, SAMPLE_TASK2);

  // List of sample tasks (via Dynamo)
  public static List<Map<String, AttributeValue>> DYNAMO_SAMPLE_TASKS =
      List.of(DYNAMO_MAP_SAMPLE_TASK1, DYNAMO_MAP_SAMPLE_TASK2);
  public static TaskSearchRequest TASK_SEARCH_REQUEST =
      TaskSearchRequest.builder()
          .searchCondition(TaskSearchCondition.EQUAL)
          .searchCriteria(TaskSearchCriteria.builder().build())
          .build();
}
