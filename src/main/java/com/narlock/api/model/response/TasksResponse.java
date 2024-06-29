package com.narlock.api.model.response;

import com.narlock.api.model.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TasksResponse {
    private List<Task> tasks;
}
