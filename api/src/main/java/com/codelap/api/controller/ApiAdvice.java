package com.codelap.api.controller;

import com.codelap.common.support.CodeLapException;
import com.codelap.common.support.UnhandledExceptionEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.BindException;
import java.util.Locale;
import java.util.NoSuchElementException;

@Log4j2
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiAdvice {

    private final MessageSource messageSource;
    private final ApplicationEventPublisher eventPublisher;

    @ExceptionHandler(Exception.class)
    public ErrorResponse exception(Exception ex) {
        log.info(ex.getMessage(), ex);

        eventPublisher.publishEvent(new UnhandledExceptionEvent(ex));

        return new ErrorResponse(new ErrorData("에러가 발생했습니다."));
    }

    @ExceptionHandler(CodeLapException.class)
    public ErrorResponse exception(CodeLapException ex) {
        log.info(ex.getMessage(), ex);

        final String message = messageSource.getMessage(ex.getMessage(), null, Locale.getDefault());

        return new ErrorResponse(new ErrorData(message));
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ErrorResponse illegalArgumentException(IllegalArgumentException ex) {
        log.info(ex.getMessage(), ex);

        return new ErrorResponse(new ErrorData("필수 값 오류입니다."));
    }

    @ExceptionHandler({IllegalStateException.class})
    public ErrorResponse illegalStateException(IllegalStateException ex) {
        log.info(ex.getMessage(), ex);

        return new ErrorResponse(new ErrorData("상태 값 오류입니다."));
    }

    @ExceptionHandler({
            BindException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
    })
    public ErrorResponse bindException(Throwable ex) {
        log.info(ex.getMessage(), ex);

        return new ErrorResponse(new ErrorData("올바르지 않은 파라미터입니다."));
    }

    @ExceptionHandler({
            HttpMediaTypeNotAcceptableException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpClientErrorException.class
    })
    public ErrorResponse httpMediaTypeNotAcceptableException(Throwable ex) {
        log.info(ex.getMessage(), ex);

        return new ErrorResponse(new ErrorData("잘못된 요청입니다."));
    }

    @ExceptionHandler({DuplicateKeyException.class})
    public ErrorResponse duplicateKeyException(DuplicateKeyException ex) {
        log.info(ex.getMessage(), ex);

        return new ErrorResponse(new ErrorData("중복 키 오류입니다."));
    }

    @ExceptionHandler({
            NoSuchElementException.class,
            EmptyResultDataAccessException.class
    })
    public ErrorResponse selectException(Throwable ex) {
        log.info(ex.getMessage(), ex);

        return new ErrorResponse(new ErrorData("특정 데이터를 조회할 수 없는 오류입니다."));
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
