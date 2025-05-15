package com.larabia.todo_api.specification;

import com.larabia.todo_api.entity.Task;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
public class TaskSpecification {

    public static Specification<Task> hasCompleted(Boolean completed) {
        return (root, query, cb) ->
                completed == null ? null : cb.equal(root.get("completed"), completed);
    }

    public static Specification<Task> hasDueDateFrom(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null : cb.greaterThanOrEqualTo(root.get("dueDate"), date);
    }

    public static Specification<Task> hasDueDateTo(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null : cb.lessThanOrEqualTo(root.get("dueDate"), date);
    }
}
