package com.todos.integration_tests.client.dao.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Base64;

import static com.todos.integration_tests.utils.GenerateUtils.generateId;

@Getter
@Builder
@ToString
public class DeleteTodoRequest {
    @Builder.Default
    private Long id = generateId();
    @Builder.Default
    private String credentials = Base64.getEncoder().encodeToString("admin:admin".getBytes());
}
