package com.todos.integration_tests.client.dao.request;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.Base64;
import java.util.HashMap;

import static com.todos.integration_tests.utils.GenerateUtils.*;
import static com.todos.integration_tests.utils.JsonUtils.stringFromJsonObject;

@Getter
@Builder
public class PutTodosRequest {
    @Builder.Default
    private Long id = generateId();
    @Builder.Default
    private String text = generateSentence();
    @Builder.Default
    private Boolean completed = generateRandomBoolean();
    @Builder.Default
    private String credentials = Base64.getEncoder().encodeToString("admin:admin".getBytes());

    public String createBody() {
        HashMap<String, Object> body = new HashMap<>();
        body.put("id", id);
        body.put("text", text);
        body.put("completed", completed);
        return stringFromJsonObject(body, JsonInclude.Include.NON_NULL);
    }
}
