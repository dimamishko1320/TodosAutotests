package com.todos.integration_tests.todo;

import com.todos.integration_tests.BaseTest;
import com.todos.integration_tests.client.TodoClient;
import com.todos.integration_tests.client.dao.request.PostTodosRequest;
import com.todos.integration_tests.dataService.entity.Todo;
import com.todos.integration_tests.dataService.service.TodoService;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.todos.integration_tests.utils.GenerateUtils.generateNegativeLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@Feature("Create a new TODO POST /todos")
class PostTodosTests extends BaseTest {
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
    void postTodos_withoutId() {
        todoClient.sendPostTodosRequest(PostTodosRequest.builder()
                        .id(null)
                        .build())
                .then()
                .statusCode(400)
                .body(containsString("Request body deserialize error: missing field `id`"));
    }

    @Description("Не передан обязательный параметр id")
    @Test
    void postTodos_withoutText() {
        todoClient.sendPostTodosRequest(PostTodosRequest.builder()
                        .text(null)
                        .build())
                .then()
                .statusCode(400)
                .body(containsString("Request body deserialize error: missing field `text`"));
    }

    @Description("Передан отрицательный id")
    @Test
    void getTodos_withoutOffset() {
        todoClient.sendPostTodosRequest(PostTodosRequest.builder()
                        .id(generateNegativeLong())
                        .build())
                .then()
                .statusCode(400)
                .body(equalTo("Invalid query string"));
    }

    @Description("В БД уже есть запись с данным id")
    @Test
    void getTodos_recordWithSameIdAlreadyExist() {
        Todo todo = todoService.saveTodo(Todo.builder().build());
        todoClient.sendPostTodosRequest(PostTodosRequest.builder()
                        .id(todo.getId())
                        .build())
                .then()
                .statusCode(400)
                .body(equalTo(""));
    }

    @Description("Успешное создание записи в бд")
    @Test
    void getTodos_successRecordCreate() {
        PostTodosRequest postTodosRequest = PostTodosRequest.builder()
                .build();
        todoClient.sendPostTodosRequest(postTodosRequest)
                .then()
                .statusCode(201);

        assertThat(todoService.getTodoById(postTodosRequest.getId()))
                .isNotNull()
                .isEqualTo(new Todo(
                        postTodosRequest.getId(),
                        postTodosRequest.getText(),
                        postTodosRequest.getCompleted()
                ));
    }

    /*todo Дописать АТ
     * 1)Не передан обязательный параметр completed
     * 2)Создание TODO с пустым text
     * 3)Создание TODO с text, содержащим 1000+ символов
     */
}
