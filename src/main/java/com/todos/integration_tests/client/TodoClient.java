package com.todos.integration_tests.client;

import com.todos.integration_tests.client.dao.request.DeleteTodoRequest;
import com.todos.integration_tests.client.dao.request.GetTodoRequest;
import com.todos.integration_tests.client.dao.request.PostTodosRequest;
import com.todos.integration_tests.client.dao.request.PutTodosRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.springframework.stereotype.Service;

@Service
public class TodoClient extends BaseClient {

    @Step("Send request GET /todos")
    public Response sendGetTodoRequest(GetTodoRequest getTodoRequest) {
        return getRequestSpecification()
                .queryParam("offset", getTodoRequest.getOffset())
                .queryParam("limit", getTodoRequest.getLimit())
                .when()
                .get(appProperties.getTodoUrl() + "/todos");
    }

    @Step("Send request POST /todos")
    public Response sendPostTodosRequest(PostTodosRequest postTodosRequest) {
        return getRequestSpecification()
                .body(postTodosRequest.createBody())
                .when()
                .post(appProperties.getTodoUrl() + "/todos");
    }

    @Step("Send request PUT /todos/:id")
    public Response sendPutTodosRequest(PutTodosRequest putTodosRequest) {
        return getRequestSpecification()
                .header("Authorization", "Basic " + putTodosRequest.getCredentials())
                .body(putTodosRequest.createBody())
                .when()
                .put(appProperties.getTodoUrl() + "/todos/" + putTodosRequest.getId());
    }

    @Step("Send request DELETE /todo/:id")
    public Response sendDeleteTodoRequest(DeleteTodoRequest deleteTodoRequest) {
        return getRequestSpecification()
                .header("Authorization", "Basic " + deleteTodoRequest.getCredentials())
                .when()
                .delete(appProperties.getTodoUrl() + "/todos/" + deleteTodoRequest.getId());
    }
}
