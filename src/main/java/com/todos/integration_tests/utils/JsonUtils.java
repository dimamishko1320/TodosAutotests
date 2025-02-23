package com.todos.integration_tests.utils;


import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;

public class JsonUtils {
    public static String stringFromJsonObject(Object payload, JsonInclude.Include rule) {
        String result = null;
        JsonInclude.Include include = rule;

        try {
            result = (new ObjectMapper()).setSerializationInclusion(include).writeValueAsString(payload);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return result;
    }
}
