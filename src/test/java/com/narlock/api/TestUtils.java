package com.narlock.api;

import com.narlock.api.model.Task;
import com.narlock.api.model.request.TaskSearchCondition;
import com.narlock.api.model.request.TaskSearchCriteria;
import com.narlock.api.model.request.TaskSearchRequest;
import java.util.Arrays;
import java.util.List;

public class TestUtils {
  public static String TASK_ID = "1";
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
  public static List<Task> SAMPLE_TASKS = List.of(SAMPLE_TASK1, SAMPLE_TASK2);
  public static TaskSearchRequest TASK_SEARCH_REQUEST =
      TaskSearchRequest.builder()
          .searchCondition(TaskSearchCondition.EQUAL)
          .searchCriteria(TaskSearchCriteria.builder().build())
          .build();
}
