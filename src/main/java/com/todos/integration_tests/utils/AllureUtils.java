package com.todos.integration_tests.utils;

import io.qameta.allure.Allure;

public class AllureUtils {
    public static Object addAttachmentEntity(Object object) {
        if (object != null) {
            Allure.addAttachment(object.getClass().getSimpleName(), object.toString());
        } else {
            nullAttachment();
        }
        return object;
    }

    public static Object nullAttachment() {
        Allure.addAttachment("Return", "null");
        return null;
    }
}
