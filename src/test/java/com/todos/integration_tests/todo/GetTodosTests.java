package com.todos.integration_tests.todo;

import com.todos.integration_tests.BaseTest;
import com.todos.integration_tests.client.TodoClient;
import com.todos.integration_tests.client.dao.request.GetTodoRequest;
import com.todos.integration_tests.client.dao.response.GetTodoResponse;
import com.todos.integration_tests.dataService.entity.Todo;
import com.todos.integration_tests.dataService.service.TodoService;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
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
    @Step("Prepare DB")
    public void clearDb() {
        todoService.deleteAllTodos();
    }

    @Description("Required offset parameter not provided")
    @Test
    void getTodos_withoutOffset() {
        todoClient.sendGetTodoRequest(GetTodoRequest.builder()
                        .offset(null)
                        .build())
                .then()
                .statusCode(400)
                .body(equalTo("Invalid query string"));
    }

    @Description("Incorrect offset provided")
    @Test
    void getTodos_incorrectOffset() {
        todoClient.sendGetTodoRequest(GetTodoRequest.builder()
                        .offset(generateNegativeInt())
                        .build())
                .then()
                .statusCode(400)
                .body(equalTo("Invalid query string"));
    }

    @Description("Required limit parameter not provided")
    @Test
    void getTodos_withoutLimit() {
        todoClient.sendGetTodoRequest(GetTodoRequest.builder()
                        .limit(null)
                        .build())
                .then()
                .statusCode(400)
                .body(equalTo("Invalid query string"));
    }

    @Description("No records in the todo table")
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

    @Description("2 records in the todo table")
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

    /*todo Add additional tests
     * 1) Incorrect limit provided (negative value)
     * 2) 3 records in DB. Provided offset = 2. Verify that records 3 and 1 are returned (they should be sorted,
     * since it is unclear how offset works)
     * 3) 1 record in DB. Provided limit = 0. Verify that 0 records are returned.
     * 4) 3 records in DB. Provided limit = 1. Verify that 2 records are returned.
     */
}
