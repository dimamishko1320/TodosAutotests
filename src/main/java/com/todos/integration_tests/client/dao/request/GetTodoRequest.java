package com.todos.integration_tests.client.dao.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetTodoRequest {
    @Builder.Default
    private Integer offset = 0;
    @Builder.Default
    private Integer limit = 10;
}
