package com.narlock.api.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskSearchCriteria {
  private String id;
  private String dueDate;
  private String title;
  private String status;
  private Integer priority;
  private String tag;
  private String userId;
}
