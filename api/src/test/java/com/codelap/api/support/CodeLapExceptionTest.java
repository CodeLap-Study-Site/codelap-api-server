package com.codelap.api.support;

import com.codelap.common.support.CodeLapException;
import com.codelap.common.support.ErrorCode;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableTypeAssert;

import static com.codelap.common.support.ErrorCode.ACTOR_VALIDATE;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public final class CodeLapExceptionTest {

    private final ErrorCode errorCode;
    private final ThrowableTypeAssert<CodeLapException> throwableTypeAssert;

    public CodeLapExceptionTest(ErrorCode errorCode, ThrowableTypeAssert<CodeLapException> throwableTypeAssert) {
        this.errorCode = errorCode;
        this.throwableTypeAssert = throwableTypeAssert;
    }

    public static CodeLapExceptionTest assertThatCodeLapException(ErrorCode errorCode) {
        return new CodeLapExceptionTest(errorCode, assertThatExceptionOfType(CodeLapException.class));
    }

    public static CodeLapExceptionTest assertThatActorValidateCodeLapException() {
        return new CodeLapExceptionTest(ACTOR_VALIDATE, assertThatExceptionOfType(CodeLapException.class));
    }

    public void isThrownBy(ThrowableAssert.ThrowingCallable throwingCallable) {
        throwableTypeAssert.isThrownBy(throwingCallable).withMessage(errorCode.getMessage());
    }
}
