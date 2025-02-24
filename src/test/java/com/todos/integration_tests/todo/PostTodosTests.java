package com.todos.integration_tests.todo;

import com.todos.integration_tests.BaseTest;
import com.todos.integration_tests.client.TodoClient;
import com.todos.integration_tests.client.dao.request.PostTodosRequest;
import com.todos.integration_tests.dataService.entity.Todo;
import com.todos.integration_tests.dataService.service.TodoService;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
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
    @Step("Prepare DB")
    public void clearDb() {
        todoService.deleteAllTodos();
    }

    @Description("Required id parameter not provided")
    @Test
    void postTodos_withoutId() {
        todoClient.sendPostTodosRequest(PostTodosRequest.builder()
                        .id(null)
                        .build())
                .then()
                .statusCode(400)
                .body(containsString("Request body deserialize error: missing field `id`"));
    }

    @Description("Required text parameter not provided")
    @Test
    void postTodos_withoutText() {
        todoClient.sendPostTodosRequest(PostTodosRequest.builder()
                        .text(null)
                        .build())
                .then()
                .statusCode(400)
                .body(containsString("Request body deserialize error: missing field `text`"));
    }

    @Description("Negative id provided")
    @Test
    void getTodos_withoutOffset() {
        todoClient.sendPostTodosRequest(PostTodosRequest.builder()
                        .id(generateNegativeLong())
                        .build())
                .then()
                .statusCode(400)
                .body(containsString("Request body deserialize error: invalid value: integer"));
    }

    @Description("Record with the same id already exists in the DB")
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

    @Description("Successful creation of a record in the DB")
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

    /*todo Add additional tests
     * 1) Required completed parameter not provided
     * 2) Creating a TODO with an empty text field
     * 3) Creating a TODO with text containing 1000+ characters
     */
}
