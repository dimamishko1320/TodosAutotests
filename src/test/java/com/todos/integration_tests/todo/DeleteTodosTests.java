package com.todos.integration_tests.todo;

import com.todos.integration_tests.BaseTest;
import com.todos.integration_tests.client.TodoClient;
import com.todos.integration_tests.client.dao.request.DeleteTodoRequest;
import com.todos.integration_tests.dataService.entity.Todo;
import com.todos.integration_tests.dataService.service.TodoService;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Delete an existing TODO DELETE /todos/:id")
public class DeleteTodosTests extends BaseTest {
    @Autowired
    private TodoClient todoClient;
    @Autowired
    private TodoService todoService;

    @BeforeEach
    public void clearDb() {
        todoService.deleteAllTodos();
    }

    @Description("Не передан обязательный параметр id")
    @Test
    void deleteTodos_withoutId() {
        todoClient.sendDeleteTodoRequest(DeleteTodoRequest.builder()
                        .id(null)
                        .build())
                .then()
                .statusCode(404);
    }

    @Description("Не передан Authorization header")
    @Test
    void deleteTodos_withoutCredentials() {
        todoClient.sendDeleteTodoRequest(DeleteTodoRequest.builder()
                        .credentials(null)
                        .build())
                .then()
                .statusCode(401);
    }

    @Description("В БД 1 todo. Удаление todo с id, который не соответствует этой todo")
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

    @Description("В БД 1 todo. Удаление данного todo")
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

    @Description("В БД 2 todo. Удаление одного todo")
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

    /*todo Дописать АТ
     * 1)Передана некорректная Authorization в хедере
     * 2)Передан отрицательный id
     */
}
