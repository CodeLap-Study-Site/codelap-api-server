package com.codelap.common.support;

public class CodeLapException extends RuntimeException {

    public CodeLapException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public CodeLapException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
    }
}
