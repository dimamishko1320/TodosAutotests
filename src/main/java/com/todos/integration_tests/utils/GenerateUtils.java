package com.todos.integration_tests.utils;

import org.instancio.Gen;
import org.instancio.Instancio;

public class GenerateUtils {
    public static Long generateId() {
        return Gen.longs().range(1L, 100000L).get();
    }

    public static Integer generateNegativeInt() {
        return Gen.ints().range(-10000, -1).get();
    }

    public static String generateSentence() {
        return Gen.text().loremIpsum().words(3).get();
    }
}
