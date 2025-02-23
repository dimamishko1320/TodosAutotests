package com.todos.integration_tests.todo;

import com.todos.integration_tests.BaseTest;
import com.todos.integration_tests.client.TodoClient;
import com.todos.integration_tests.client.dao.request.GetTodoRequest;

import com.todos.integration_tests.client.dao.response.GetTodoResponse;
import com.todos.integration_tests.dataService.entity.Todo;
import com.todos.integration_tests.dataService.service.TodoService;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.todos.integration_tests.utils.GenerateUtils.generateNegativeInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Feature("Get a JSON list of TODOs GET /todos")
class GetTodosTests extends BaseTest {
    @Autowired
    private TodoClient todoClient;
    @Autowired
    private TodoService todoService;

    @BeforeEach
    public void clearDb() {
        todoService.deleteAllTodos();
    }

    @Description("Не передан обязательный параметр offset")
    @Test
    void getTodos_withoutOffset() {
        todoClient.sendGetTodoRequest(GetTodoRequest.builder()
                        .offset(null)
                        .build())
                .then()
                .statusCode(400)
                .body(equalTo("Invalid query string"));
    }

    @Description("Передан некорректный offset")
    @Test
    void getTodos_incorrectOffset() {
        todoClient.sendGetTodoRequest(GetTodoRequest.builder()
                        .offset(generateNegativeInt())
                        .build())
                .then()
                .statusCode(400)
                .body(equalTo("Invalid query string"));
    }

    @Description("Не передан обязательный параметр limit")
    @Test
    void getTodos_withoutLimit() {
        todoClient.sendGetTodoRequest(GetTodoRequest.builder()
                        .limit(null)
                        .build())
                .then()
                .statusCode(400)
                .body(equalTo("Invalid query string"));
    }

    @Description("В таблице todo нет записей")
    @Test
    void getTodos_withoutRecordInTodo() {
        List<GetTodoResponse> todoResponses = todoClient.sendGetTodoRequest(GetTodoRequest.builder().build())
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", GetTodoResponse.class);

        assertThat(todoResponses)
                .isEmpty();
    }

    @Description("В таблице todo 2 записи")
    @Test
    void getTodos_twoRecordInDb() {
        Todo todoFirst = todoService.saveTodo(Todo.builder().build());
        Todo todoSecond = todoService.saveTodo(Todo.builder().build());

        List<GetTodoResponse> todoResponses = todoClient.sendGetTodoRequest(GetTodoRequest.builder().build())
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", GetTodoResponse.class);

        assertThat(todoResponses)
                .hasSize(2)
                .contains(
                        new GetTodoResponse(todoFirst),
                        new GetTodoResponse(todoSecond)
                );
    }

    /*todo Дописать АТ
    * 1)Передан некорректный limit(отрицательный)
    * 2)В БД 3 записи. Передан offset = 2. Проверка, что нам вернется 3 и 1 запись(видимо они должны возвращаться
    * отсортированные, т.к. не понятно, как offset работает)
    * 3)В БД 1 запись. Передан limit = 0. Проверка, что нам вернулось 0 записей.
    * 4)В БД 3 записи. Передан limit = 1. Проверка того, что нам вернулось 2 записи.
     */
}
