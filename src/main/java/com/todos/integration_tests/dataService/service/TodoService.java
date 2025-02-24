package com.todos.integration_tests.dataService.service;

import com.todos.integration_tests.client.TodoClient;
import com.todos.integration_tests.client.dao.request.DeleteTodoRequest;
import com.todos.integration_tests.client.dao.request.GetTodoRequest;
import com.todos.integration_tests.client.dao.request.PostTodosRequest;
import com.todos.integration_tests.client.dao.response.GetTodoResponse;
import com.todos.integration_tests.dataService.entity.Todo;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoService {
    @Autowired
    private TodoClient todoClient;

    @Step("Save todo in DB")
    public Todo saveTodo(Todo todo) {
        todoClient.sendPostTodosRequest(PostTodosRequest.builder()
                .id(todo.getId())
                .text(todo.getText())
                .completed(todo.getCompleted())
                .build());
        return todo;
    }

    @Step("Delete todo from DB")
    public Todo getTodoById(Long id) {
        GetTodoResponse getTodoResponse = todoClient.sendGetTodoRequest(GetTodoRequest.builder()
                        .limit(100)
                        .offset(0)
                        .build())
                .then()
                .extract()
                .body()
                .jsonPath()
                .getList(".", GetTodoResponse.class)
                .stream()
                .filter(todo -> todo.getId().equals(id))
                .findFirst()
                .orElse(null);
        return new Todo(getTodoResponse.getId(), getTodoResponse.getText(), getTodoResponse.isCompleted());
    }

    @Step("Delete todo from DB")
    public void deleteTodoById(Long id) {
        todoClient.sendDeleteTodoRequest(DeleteTodoRequest.builder()
                .id(id)
                .build());
    }

    @Step("Delete all todos from DB")
    public void deleteAllTodos() {
        List<Long> ids = todoClient.sendGetTodoRequest(GetTodoRequest.builder()
                        .limit(1000)
                        .build())
                .then()
                .extract()
                .jsonPath()
                .getList("id")
                .stream()
                .map(v -> Long.valueOf((int) v))
                .collect(Collectors.toList());
        for (Long id : ids) {
            todoClient.sendDeleteTodoRequest(DeleteTodoRequest.builder()
                    .id(id)
                    .build());
        }
    }
}
