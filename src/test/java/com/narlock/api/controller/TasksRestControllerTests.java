package com.narlock.api.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.narlock.api.model.Task;
import com.narlock.api.service.TasksService;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TasksRestController.class)
public class TasksRestControllerTests {

  @Autowired private MockMvc mockMvc;

  @MockBean private TasksService tasksService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetTasks() throws Exception {
    Task task1 =
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

    Task task2 =
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

    given(tasksService.getTasks()).willReturn(Arrays.asList(task1, task2));

    mockMvc
        .perform(get("/tasks").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.tasks[0].id").value("1"))
        .andExpect(jsonPath("$.tasks[0].title").value("Complete project documentation"))
        .andExpect(
            jsonPath("$.tasks[0].description")
                .value("Finalize and submit the project documentation by the end of the week."))
        .andExpect(jsonPath("$.tasks[0].status").value("pending"))
        .andExpect(jsonPath("$.tasks[0].priority").value("2"))
        .andExpect(jsonPath("$.tasks[0].dueDate").value("2024-07-05"))
        .andExpect(jsonPath("$.tasks[0].tags[0]").value("work"))
        .andExpect(jsonPath("$.tasks[0].tags[1]").value("documentation"))
        .andExpect(jsonPath("$.tasks[0].userId").value("user-123"))
        .andExpect(jsonPath("$.tasks[1].id").value("2"))
        .andExpect(jsonPath("$.tasks[1].title").value("Grocery shopping"))
        .andExpect(
            jsonPath("$.tasks[1].description")
                .value(
                    "Buy groceries for the week, including fruits, vegetables, and dairy products."))
        .andExpect(jsonPath("$.tasks[1].status").value("in-progress"))
        .andExpect(jsonPath("$.tasks[1].priority").value("1"))
        .andExpect(jsonPath("$.tasks[1].dueDate").value("2024-07-03"))
        .andExpect(jsonPath("$.tasks[1].tags[0]").value("personal"))
        .andExpect(jsonPath("$.tasks[1].tags[1]").value("shopping"))
        .andExpect(jsonPath("$.tasks[1].userId").value("user-456"));
  }
}
