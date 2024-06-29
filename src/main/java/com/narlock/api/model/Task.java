package com.narlock.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    private String id; // PK
    private String dueDate; // SK
    private String title;
    private String description;
    private String status;
    private Integer priority;
    private List<String> tags;
    private String userId;
}
