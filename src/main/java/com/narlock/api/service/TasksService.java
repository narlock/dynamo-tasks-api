package com.narlock.api.service;

import com.narlock.api.mapper.TasksMapper;
import com.narlock.api.model.Task;
import com.narlock.api.model.exception.ItemNotFoundException;
import com.narlock.api.model.request.TaskSearchCondition;
import com.narlock.api.model.request.TaskSearchCriteria;
import com.narlock.api.model.request.TaskSearchRequest;
import com.narlock.api.util.TasksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@Service
public class TasksService {
    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Autowired
    private TasksMapper tasksMapper;

    /**
     * Retrieves each task in the Tasks table interfaced through DynamoDB
     * @return
     */
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();

        // Create a ScanRequest to retrieve all items from the "Tasks" table
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName("Tasks")
                .build();

        // Perform the scan operation
        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);

        // Iterate over the items returned by the scan operation
        for (Map<String, AttributeValue> item : scanResponse.items()) {
            // Convert the item map to a Task object
            Task task = tasksMapper.convertMapToTask(item);
            tasks.add(task);
        }

        return tasks;
    }

    /**
     * Retrieves a single task by from DynamoDB by its taskId
     * @param taskId
     * @return
     */
    public Task getTaskById(String taskId) {
        // Define the scan filter expression
        String filterExpression = "PK = :PK";

        // Define the expression attribute values
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":PK", AttributeValue.builder().s(taskId).build());

        // Create a ScanRequest
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName("Tasks")
                .filterExpression(filterExpression)
                .expressionAttributeValues(expressionAttributeValues)
                .build();

        // Execute the ScanRequest
        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);

        // Check if the item exists
        if (scanResponse.items().isEmpty()) {
            throw new ItemNotFoundException("Task " + taskId + " was not found");
        }

        // Convert the response item to a Task object
        Map<String, AttributeValue> item = scanResponse.items().get(0);
        return tasksMapper.convertMapToTask(item);
    }

    /**
     * Given a searchRequest, this method retrieves tasks from DynamoDB that meet the
     * search criteria and search condition specified in the searchRequest.
     * @param searchRequest
     * @return list of tasks matching search request
     */
    public List<Task> searchTasks(TaskSearchRequest searchRequest) {
        TaskSearchCriteria criteria = searchRequest.getSearchCriteria();
        TaskSearchCondition condition = searchRequest.getSearchCondition();

        // Validate that at least one criterion is provided
        if (criteria == null || TasksUtils.allCriteriaAreNull(criteria)) {
            throw new IllegalArgumentException("At least one search criterion must be provided.");
        }

        // Build the filter expression and attribute values
        StringBuilder filterExpression = new StringBuilder();
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();

        addFilterExpression(criteria.getId(), "PK", condition, filterExpression, expressionAttributeValues);
        addFilterExpression(criteria.getDueDate(), "SK", condition, filterExpression, expressionAttributeValues);
        addFilterExpression(criteria.getTitle(), "title", condition, filterExpression, expressionAttributeValues);
        addFilterExpression(criteria.getStatus(), "status", condition, filterExpression, expressionAttributeValues);
        addFilterExpression(criteria.getPriority() != null ? String.valueOf(criteria.getPriority()) : null, "priority", condition, filterExpression, expressionAttributeValues);
        addFilterExpression(criteria.getTag(), "tags", condition, filterExpression, expressionAttributeValues);
        addFilterExpression(criteria.getUserId(), "userId", condition, filterExpression, expressionAttributeValues);

        // Create a ScanRequest
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName("Tasks")
                .filterExpression(filterExpression.toString())
                .expressionAttributeValues(expressionAttributeValues)
                .build();

        // Execute the ScanRequest
        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);

        // Convert the response items to Task objects
        List<Task> tasks = new ArrayList<>();
        for (Map<String, AttributeValue> item : scanResponse.items()) {
            Task task = tasksMapper.convertMapToTask(item);
            tasks.add(task);
        }

        return tasks;
    }

    /**
     * A helper method for adding a filter expression to a search call
     * @param value
     * @param attributeName
     * @param condition
     * @param filterExpression
     * @param expressionAttributeValues
     */
    private void addFilterExpression(String value, String attributeName, TaskSearchCondition condition,
                                     StringBuilder filterExpression, Map<String, AttributeValue> expressionAttributeValues) {
        if (value != null) {
            if (filterExpression.length() > 0) {
                filterExpression.append(" AND ");
            }
            String attributeKey = ":" + attributeName;
            if (condition == TaskSearchCondition.EQUAL) {
                filterExpression.append(attributeName).append(" = ").append(attributeKey);
            } else if (condition == TaskSearchCondition.BEGINS_WITH) {
                filterExpression.append("begins_with(").append(attributeName).append(", ").append(attributeKey).append(")");
            }
            expressionAttributeValues.put(attributeKey, AttributeValue.builder().s(value).build());
        }
    }

    /**
     * Creates a task in the Tasks table on DynamoDB
     * @param task
     * @return
     */
    public Task createTask(Task task) {
        // Generate a unique ID for the task
        String taskId;
        do {
            taskId = UUID.randomUUID().toString();
        } while (taskIdExists(taskId));

        task.setId(taskId);

        // Create a map of attribute values
        Map<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("PK", AttributeValue.builder().s(task.getId()).build());
        itemValues.put("SK", AttributeValue.builder().s(task.getDueDate()).build());
        itemValues.put("title", AttributeValue.builder().s(task.getTitle()).build());
        itemValues.put("description", AttributeValue.builder().s(task.getDescription()).build());
        itemValues.put("status", AttributeValue.builder().s(task.getStatus()).build());
        itemValues.put("priority", AttributeValue.builder().n(String.valueOf(task.getPriority())).build());
        itemValues.put("userId", AttributeValue.builder().s(task.getUserId()).build());
        itemValues.put("tags", AttributeValue.builder().ss(task.getTags()).build());

        // Create a PutItemRequest
        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName("Tasks")
                .item(itemValues)
                .build();

        // Execute the PutItemRequest
        dynamoDbClient.putItem(putItemRequest);

        return getTaskById(taskId);
    }

    /**
     * A helper method to ensure a taskId is strictly unique
     * @param taskId
     * @return true if the taskId exists in the DynamoDB
     */
    private boolean taskIdExists(String taskId) {
        // Define the scan filter expression
        String filterExpression = "PK = :taskId";

        // Define the expression attribute values
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":taskId", AttributeValue.builder().s(taskId).build());

        // Create a ScanRequest
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName("Tasks")
                .filterExpression(filterExpression)
                .expressionAttributeValues(expressionAttributeValues)
                .build();

        // Execute the ScanRequest
        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);

        // Check if item exists
        return !scanResponse.items().isEmpty();
    }
}
