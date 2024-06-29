package com.narlock.api.model.response;

import com.narlock.api.model.Task;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TasksResponse {
  private List<Task> tasks;
}
