package com.codelap.api.controller;

import com.codelap.common.support.CodeLapException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApiAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ErrorResponse exception(Exception ex) {
        return new ErrorResponse(new ErrorData("에러가 발생했습니다."));
    }

    @ExceptionHandler(CodeLapException.class)
    public ErrorResponse exception(CodeLapException ex) {
        final String message = messageSource.getMessage(ex.getMessage(), null, Locale.getDefault());

        return new ErrorResponse(new ErrorData(message));
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorResponse {
        private ErrorData error;
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorData {
        private String message;
    }
}
