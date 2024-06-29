package com.narlock.api.util;

import com.narlock.api.model.request.TaskSearchCriteria;

public class TasksUtils {
    public static boolean allCriteriaAreNull(TaskSearchCriteria criteria) {
        return criteria.getId() == null &&
                criteria.getDueDate() == null &&
                criteria.getTitle() == null &&
                criteria.getStatus() == null &&
                criteria.getPriority() == null &&
                criteria.getTag() == null &&
                criteria.getUserId() == null;
    }
}
