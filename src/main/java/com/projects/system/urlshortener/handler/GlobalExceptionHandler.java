package com.projects.system.urlshortener.handler;

import com.projects.system.urlshortener.exception.UrlExpiredException;
import com.projects.system.urlshortener.exception.UrlNotFoundException;
import com.projects.system.urlshortener.exception.UrlServiceError;
import com.projects.system.urlshortener.exception.UrlServiceException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String X_CORRELATION_ID = "X-Correlation-Id";

    @ExceptionHandler(value = UrlExpiredException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UrlServiceError> handleUrlExpiredException(UrlExpiredException e, HttpServletRequest request) {
        UrlServiceError urlServiceError = new UrlServiceError(request.getHeader(X_CORRELATION_ID), "U0001", e.getMessage(), OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.GONE).header(X_CORRELATION_ID, request.getHeader(X_CORRELATION_ID)).body(urlServiceError);
    }

    @ExceptionHandler(value = UrlNotFoundException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UrlServiceError> handleUrlNotFoundException(UrlNotFoundException e, HttpServletRequest request) {
        UrlServiceError urlServiceError = new UrlServiceError(request.getHeader(X_CORRELATION_ID), "U0002", e.getMessage(), OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).header(X_CORRELATION_ID, request.getHeader(X_CORRELATION_ID)).body(urlServiceError);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UrlServiceError> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<UrlServiceError.FieldError> fieldErrors = e.getBindingResult().getFieldErrors().stream().map(err -> new UrlServiceError.FieldError(err.getField(), err.getDefaultMessage())).collect(Collectors.toList());
        UrlServiceError urlServiceError = new UrlServiceError(request.getHeader(X_CORRELATION_ID), "U0003", "Invalid request body", fieldErrors, OffsetDateTime.now());
        return ResponseEntity.badRequest().header(X_CORRELATION_ID, request.getHeader(X_CORRELATION_ID)).body(urlServiceError);
    }

    @ExceptionHandler(value = UrlServiceException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UrlServiceError> handleServiceException(UrlServiceException e, HttpServletRequest request) {
        UrlServiceError urlServiceError = new UrlServiceError(request.getHeader(X_CORRELATION_ID), "U0004", e.getMessage(), OffsetDateTime.now());
        return ResponseEntity.badRequest().header(X_CORRELATION_ID, request.getHeader(X_CORRELATION_ID)).body(urlServiceError);
    }

    @ExceptionHandler(value = Exception.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UrlServiceError> handleGeneralException(Exception e, HttpServletRequest request) {
        UrlServiceError urlServiceError = new UrlServiceError(request.getHeader(X_CORRELATION_ID), "U9999", e.getMessage(), OffsetDateTime.now());
        return ResponseEntity.internalServerError().header(X_CORRELATION_ID, request.getHeader(X_CORRELATION_ID)).body(urlServiceError);
    }
}
