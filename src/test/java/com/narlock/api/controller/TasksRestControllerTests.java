package com.narlock.api.controller;

import static com.narlock.api.TestUtils.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.narlock.api.model.exception.ItemNotFoundException;
import com.narlock.api.service.TasksService;
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

  ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetTasks_200() throws Exception {
    given(tasksService.getTasks()).willReturn(SAMPLE_TASKS);

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

  @Test
  public void testGetTasks_500() throws Exception {
    given(tasksService.getTasks()).willThrow(RuntimeException.class);

    mockMvc
        .perform(get("/tasks").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value("500"))
        .andExpect(jsonPath("$.error").value("Internal Server Error"))
        .andExpect(jsonPath("$.path").value("uri=/tasks"));
  }

  @Test
  public void testGetTaskById_200() throws Exception {
    given(tasksService.getTaskById(TASK_ID)).willReturn(SAMPLE_TASK1);

    mockMvc
        .perform(get("/tasks/" + TASK_ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.title").value("Complete project documentation"))
        .andExpect(
            jsonPath("$.description")
                .value("Finalize and submit the project documentation by the end of the week."))
        .andExpect(jsonPath("$.status").value("pending"))
        .andExpect(jsonPath("$.priority").value("2"))
        .andExpect(jsonPath("$.dueDate").value("2024-07-05"))
        .andExpect(jsonPath("$.tags[0]").value("work"))
        .andExpect(jsonPath("$.tags[1]").value("documentation"))
        .andExpect(jsonPath("$.userId").value("user-123"));
  }

  @Test
  public void testGetTaskById_404() throws Exception {
    given(tasksService.getTaskById(TASK_ID))
        .willThrow(new ItemNotFoundException("Task " + TASK_ID + " was not found"));

    mockMvc
        .perform(get("/tasks/" + TASK_ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value("404"))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Task 1 was not found"))
        .andExpect(jsonPath("$.path").value("uri=/tasks/1"));
  }

  @Test
  public void testGetTaskById_500() throws Exception {
    given(tasksService.getTaskById(TASK_ID)).willThrow(RuntimeException.class);

    mockMvc
        .perform(get("/tasks/" + TASK_ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value("500"))
        .andExpect(jsonPath("$.error").value("Internal Server Error"))
        .andExpect(jsonPath("$.path").value("uri=/tasks/1"));
  }

  @Test
  public void testSearchTasks_200() throws Exception {
    String requestBody = objectMapper.writeValueAsString(TASK_SEARCH_REQUEST);
    given(tasksService.searchTasks(TASK_SEARCH_REQUEST)).willReturn(SAMPLE_TASKS);

    mockMvc
        .perform(post("/tasks/search").contentType(MediaType.APPLICATION_JSON).content(requestBody))
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

  @Test
  public void testSearchTasks_400() throws Exception {
    String requestBody = objectMapper.writeValueAsString(TASK_SEARCH_REQUEST);
    given(tasksService.searchTasks(TASK_SEARCH_REQUEST)).willThrow(IllegalArgumentException.class);

    mockMvc
        .perform(post("/tasks/search").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value("400"))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.path").value("uri=/tasks/search"));
  }

  @Test
  public void testSearchTasks_500() throws Exception {
    String requestBody = objectMapper.writeValueAsString(TASK_SEARCH_REQUEST);
    given(tasksService.searchTasks(TASK_SEARCH_REQUEST)).willThrow(RuntimeException.class);

    mockMvc
        .perform(post("/tasks/search").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value("500"))
        .andExpect(jsonPath("$.error").value("Internal Server Error"))
        .andExpect(jsonPath("$.path").value("uri=/tasks/search"));
  }

  @Test
  public void testCreateTask_201() throws Exception {
    String requestBody = objectMapper.writeValueAsString(SAMPLE_TASK1);
    given(tasksService.createTask(SAMPLE_TASK1)).willReturn(SAMPLE_TASK1);

    mockMvc
        .perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.title").value("Complete project documentation"))
        .andExpect(
            jsonPath("$.description")
                .value("Finalize and submit the project documentation by the end of the week."))
        .andExpect(jsonPath("$.status").value("pending"))
        .andExpect(jsonPath("$.priority").value("2"))
        .andExpect(jsonPath("$.dueDate").value("2024-07-05"))
        .andExpect(jsonPath("$.tags[0]").value("work"))
        .andExpect(jsonPath("$.tags[1]").value("documentation"))
        .andExpect(jsonPath("$.userId").value("user-123"));
  }

  @Test
  public void testCreateTask_500() throws Exception {
    String requestBody = objectMapper.writeValueAsString(SAMPLE_TASK1);
    given(tasksService.createTask(SAMPLE_TASK1)).willThrow(RuntimeException.class);

    mockMvc
        .perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value("500"))
        .andExpect(jsonPath("$.error").value("Internal Server Error"))
        .andExpect(jsonPath("$.path").value("uri=/tasks"));
  }

  @Test
  public void testOverwriteTask_200() throws Exception {
    String requestBody = objectMapper.writeValueAsString(SAMPLE_TASK1);
    given(tasksService.overwriteTask(TASK_ID, SAMPLE_TASK1)).willReturn(SAMPLE_TASK1);

    mockMvc
        .perform(
            put("/tasks/" + TASK_ID).contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.title").value("Complete project documentation"))
        .andExpect(
            jsonPath("$.description")
                .value("Finalize and submit the project documentation by the end of the week."))
        .andExpect(jsonPath("$.status").value("pending"))
        .andExpect(jsonPath("$.priority").value("2"))
        .andExpect(jsonPath("$.dueDate").value("2024-07-05"))
        .andExpect(jsonPath("$.tags[0]").value("work"))
        .andExpect(jsonPath("$.tags[1]").value("documentation"))
        .andExpect(jsonPath("$.userId").value("user-123"));
  }

  @Test
  public void testOverwriteTask_404() throws Exception {
    String requestBody = objectMapper.writeValueAsString(SAMPLE_TASK1);
    given(tasksService.overwriteTask(TASK_ID, SAMPLE_TASK1))
        .willThrow(new ItemNotFoundException("Task " + TASK_ID + " was not found"));

    mockMvc
        .perform(
            put("/tasks/" + TASK_ID).contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isNotFound())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value("404"))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Task 1 was not found"))
        .andExpect(jsonPath("$.path").value("uri=/tasks/1"));
  }

  @Test
  public void testOverwriteTask_400() throws Exception {
    String requestBody = objectMapper.writeValueAsString(SAMPLE_TASK1);
    given(tasksService.overwriteTask(TASK_ID, SAMPLE_TASK1))
        .willThrow(IllegalArgumentException.class);

    mockMvc
        .perform(
            put("/tasks/" + TASK_ID).contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value("400"))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.path").value("uri=/tasks/1"));
  }

  @Test
  public void testOverwriteTask_500() throws Exception {
    String requestBody = objectMapper.writeValueAsString(SAMPLE_TASK1);
    given(tasksService.overwriteTask(TASK_ID, SAMPLE_TASK1)).willThrow(RuntimeException.class);

    mockMvc
        .perform(
            put("/tasks/" + TASK_ID).contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value("500"))
        .andExpect(jsonPath("$.error").value("Internal Server Error"))
        .andExpect(jsonPath("$.path").value("uri=/tasks/1"));
  }

  @Test
  public void testDeleteTask_204() throws Exception {
    mockMvc
        .perform(delete("/tasks/" + TASK_ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }
}
