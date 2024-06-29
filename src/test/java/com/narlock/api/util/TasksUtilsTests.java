package com.narlock.api.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.narlock.api.model.request.TaskSearchCriteria;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class TasksUtilsTests {

  static Stream<TaskSearchCriteria> provideCriteria() {
    return Stream.of(
        // All fields null
        TaskSearchCriteria.builder()
            .id(null)
            .dueDate(null)
            .title(null)
            .status(null)
            .priority(null)
            .tag(null)
            .userId(null)
            .build(),

        // One field not null (e.g., title)
        TaskSearchCriteria.builder()
            .id(null)
            .dueDate(null)
            .title("Sample Task")
            .status(null)
            .priority(null)
            .tag(null)
            .userId(null)
            .build(),

        // Another field not null (e.g., status)
        TaskSearchCriteria.builder()
            .id(null)
            .dueDate(null)
            .title(null)
            .status("Pending")
            .priority(null)
            .tag(null)
            .userId(null)
            .build(),

        // Multiple fields not null
        TaskSearchCriteria.builder()
            .id(null)
            .dueDate(null)
            .title("Sample Task")
            .status("Pending")
            .priority(null)
            .tag(null)
            .userId(null)
            .build(),

        // All fields non-null
        TaskSearchCriteria.builder()
            .id("123")
            .dueDate("2024-07-01")
            .title("Sample Task")
            .status("Pending")
            .priority(1)
            .tag("Work")
            .userId("user123")
            .build());
  }

  @ParameterizedTest
  @MethodSource("provideCriteria")
  public void testAllCriteriaAreNull(TaskSearchCriteria criteria) {
    boolean result = TasksUtils.allCriteriaAreNull(criteria);

    if (criteria.getId() == null
        && criteria.getDueDate() == null
        && criteria.getTitle() == null
        && criteria.getStatus() == null
        && criteria.getPriority() == null
        && criteria.getTag() == null
        && criteria.getUserId() == null) {
      assertTrue(result);
    } else {
      assertFalse(result);
    }
  }
}
