package com.todos.integration_tests.todo;

import com.todos.integration_tests.BaseTest;
import com.todos.integration_tests.client.TodoClient;
import com.todos.integration_tests.client.dao.request.PutTodosRequest;
import com.todos.integration_tests.dataService.entity.Todo;
import com.todos.integration_tests.dataService.service.TodoService;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;

@Feature("Update an existing TODO with the provided one PUT /todos/:id")
public class PutTodosTests extends BaseTest {
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
    void putTodos_withoutId() {
        todoClient.sendPutTodosRequest(PutTodosRequest.builder()
                        .id(null)
                        .build())
                .then()
                .statusCode(404)
                .body(containsString(""));
    }

    @Description("Не передан обязательный параметр text")
    @Test
    void putTodos_withoutText() {
        todoClient.sendPutTodosRequest(PutTodosRequest.builder()
                        .text(null)
                        .build())
                .then()
                .statusCode(400)
                .body(containsString("Request body deserialize error: missing field `text`"));
    }

    @Description("Не передан обязательный параметр completed")
    @Test
    void putTodos_withoutCompleted() {
        todoClient.sendPutTodosRequest(PutTodosRequest.builder()
                        .completed(null)
                        .build())
                .then()
                .statusCode(400)
                .body(containsString("Request body deserialize error: missing field `text`"));
    }

    @Description("В бд есть запись 1 запись для данного id.")
    @Test
    void putTodos_recordInDbExist() {
        Todo todo = todoService.saveTodo(Todo.builder().build());

        PutTodosRequest putTodosRequest = PutTodosRequest.builder()
                .id(todo.getId())
                .build();
        todoClient.sendPutTodosRequest(putTodosRequest)
                .then()
                .statusCode(200);

        assertThat(todoService.getAllTodos())
                .hasSize(1)
                .contains(new Todo(
                        putTodosRequest.getId(),
                        putTodosRequest.getText(),
                        putTodosRequest.getCompleted()
                ));
    }

    @Description("В бд отсутствует запись для данного id")
    @Test
    void putTodos_recordInDbExistForThisId() {
        Todo todo = todoService.saveTodo(Todo.builder().build());

        PutTodosRequest putTodosRequest = PutTodosRequest.builder()
                .build();
        todoClient.sendPutTodosRequest(putTodosRequest)
                .then()
                .statusCode(404);

        assertThat(todoService.getAllTodos())
                .hasSize(1)
                .contains(todo);
    }

    /*todo Дописать АТ
     * 1)Обновление TODO пустым text
     * 2)Обновление TODO text, содержащим 1000+ символов
     * 3)Не передан Authorization в хедере
     * 4)Передана некорректная Authorization в хедере
     */
}
