package com.todos.integration_tests.dataService.entity;

import lombok.*;

import static com.todos.integration_tests.utils.GenerateUtils.generateId;
import static com.todos.integration_tests.utils.GenerateUtils.generateSentence;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Todo {
    @Builder.Default
    private Long id = generateId();
    @Builder.Default
    private String text = generateSentence();
    @Builder.Default
    private Boolean completed = true;
}
