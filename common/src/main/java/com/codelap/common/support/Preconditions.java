package com.codelap.common.support;


import java.util.Objects;

public class Preconditions {

    public static <T> T notNull(T obj) {
        return Objects.requireNonNull(obj);
    }

    public static void require(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    public static void require(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void check(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }

    public static void check(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

}
