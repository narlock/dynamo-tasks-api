package com.narlock.api.mapper;

import com.narlock.api.model.Task;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Component
public class TasksMapper {
  public Task convertMapToTask(Map<String, AttributeValue> item) {
    return Task.builder()
        .id(item.get("PK").s())
        .dueDate(item.get("SK").s())
        .title(item.get("title").s())
        .description(item.get("description").s())
        .status(item.get("status").s())
        .priority(Integer.valueOf(item.get("priority").n()))
        .tags(item.containsKey("tags") ? item.get("tags").ss() : new ArrayList<>())
        .userId(item.get("userId").s())
        .build();
  }
}
