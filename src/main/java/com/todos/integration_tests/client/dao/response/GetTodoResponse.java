package com.todos.integration_tests.client.dao.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.todos.integration_tests.dataService.entity.Todo;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class GetTodoResponse {
    private Long id;

    private String text;

    private boolean completed;

    public GetTodoResponse(Todo todo) {
        id = todo.getId();
        text = todo.getText();
        completed = todo.getCompleted();
    }
}
