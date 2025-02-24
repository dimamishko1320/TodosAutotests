package com.todos.integration_tests.todo;

import com.todos.integration_tests.BaseTest;
import com.todos.integration_tests.client.TodoClient;
import com.todos.integration_tests.client.dao.request.PutTodosRequest;
import com.todos.integration_tests.dataService.entity.Todo;
import com.todos.integration_tests.dataService.service.TodoService;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;

@Feature("Update an existing TODO with the provided one PUT /todos/:id")
class PutTodosTests extends BaseTest {
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
    void putTodos_withoutId() {
        todoClient.sendPutTodosRequest(PutTodosRequest.builder()
                        .id(null)
                        .build())
                .then()
                .statusCode(404)
                .body(containsString(""));
    }

    @Description("Required text parameter not provided")
    @Test
    void putTodos_withoutText() {
        todoClient.sendPutTodosRequest(PutTodosRequest.builder()
                        .text(null)
                        .build())
                .then()
                .statusCode(400)
                .body(containsString("Request body deserialize error: missing field `text`"));
    }

    @Description("Required completed parameter not provided")
    @Test
    void putTodos_withoutCompleted() {
        todoClient.sendPutTodosRequest(PutTodosRequest.builder()
                        .completed(null)
                        .build())
                .then()
                .statusCode(400)
                .body(containsString("Request body deserialize error: missing field `completed`"));
    }

    @Description("Record for the given id exists in the DB.")
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

    @Description("Record for the given id does not exist in the DB")
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

    /*todo Add additional tests
     * 1) Updating TODO with an empty text field
     * 2) Updating TODO with text containing 1000+ characters
     * 3) Authorization header not provided
     * 4) Invalid Authorization header provided
     */
}