package com.todos.integration_tests.todo;

import com.todos.integration_tests.BaseTest;
import com.todos.integration_tests.client.TodoClient;
import com.todos.integration_tests.client.dao.request.DeleteTodoRequest;
import com.todos.integration_tests.dataService.entity.Todo;
import com.todos.integration_tests.dataService.service.TodoService;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Delete an existing TODO DELETE /todos/:id")
class DeleteTodosTests extends BaseTest {
    @Autowired
    private TodoClient todoClient;
    @Autowired
    private TodoService todoService;

    @BeforeEach
    @Step("Prepare DB")
    public void clearDb() {
        todoService.deleteAllTodos();
    }

    @Description("Required id parameter not provided")
    @Test
    void deleteTodos_withoutId() {
        todoClient.sendDeleteTodoRequest(DeleteTodoRequest.builder()
                        .id(null)
                        .build())
                .then()
                .statusCode(404);
    }

    @Description("Authorization header not provided")
    @Test
    void deleteTodos_withoutCredentials() {
        todoClient.sendDeleteTodoRequest(DeleteTodoRequest.builder()
                        .credentials(null)
                        .build())
                .then()
                .statusCode(401);
    }

    @Description("1 todo in DB. Deleting todo with an id that does not match this todo")
    @Test
    void deleteTodos_idForAnotherTodo() {
        Todo todo = todoService.saveTodo(Todo.builder().build());

        todoClient.sendDeleteTodoRequest(DeleteTodoRequest.builder()
                        .build())
                .then()
                .statusCode(404);

        assertThat(todoService.getTodoById(todo.getId()))
                .isNotNull()
                .isEqualTo(todo);
    }

    @Description("1 todo in DB. Deleting this todo")
    @Test
    void deleteTodos_idForThisTodo() {
        Todo todo = todoService.saveTodo(Todo.builder().build());

        todoClient.sendDeleteTodoRequest(DeleteTodoRequest.builder()
                        .id(todo.getId())
                        .build())
                .then()
                .statusCode(204);

        assertThat(todoService.getTodoById(todo.getId()))
                .isNull();
    }

    @Description("2 todos in DB. Deleting one todo")
    @Test
    void deleteTodos_twoRecordsInDb() {
        Todo todoFirst = todoService.saveTodo(Todo.builder().build());
        Todo todoSecond = todoService.saveTodo(Todo.builder().build());

        todoClient.sendDeleteTodoRequest(DeleteTodoRequest.builder()
                        .id(todoFirst.getId())
                        .build())
                .then()
                .statusCode(204);

        assertThat(todoService.getAllTodos())
                .hasSize(1)
                .contains(todoSecond);
    }

    /*todo Add additional tests
     * 1) Incorrect Authorization provided in the header
     * 2) Negative id provided
     */
}