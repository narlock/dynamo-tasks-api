package com.narlock.api.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskSearchRequest {
  private TaskSearchCriteria searchCriteria;
  private TaskSearchCondition searchCondition;
}
