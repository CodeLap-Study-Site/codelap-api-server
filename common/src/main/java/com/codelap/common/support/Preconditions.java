package com.codelap.common.support;


import static com.codelap.common.support.ErrorCode.ACTOR_VALIDATE;

public class Preconditions {

    public static void validate(boolean expression, ErrorCode errorCode) {
        if (!expression) {
            throw new CodeLapException(errorCode);
        }
    }

    public static void actorValidate(boolean expression) {
        if (!expression) {
            throw new CodeLapException(ACTOR_VALIDATE);
        }
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
