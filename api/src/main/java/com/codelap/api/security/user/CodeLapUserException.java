package com.codelap.api.security.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CodeLapUserException extends RuntimeException {

    private String message;
    private HttpStatus status;

    public CodeLapUserException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
