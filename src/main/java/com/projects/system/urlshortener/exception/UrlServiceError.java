package com.projects.system.urlshortener.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

public record UrlServiceError(
    @JsonProperty("correlation_id") String correlationId,
    @JsonProperty("error_code") String errorCode,
    @JsonProperty("error_message") String errorMessage,
    @JsonProperty("errors") List<FieldError> errors,
    @JsonProperty("timestamp") OffsetDateTime timestamp
) {
    public UrlServiceError(String correlationId, String errorCode, String errorMessage, OffsetDateTime timestamp) {
        this(correlationId, errorCode, errorMessage, null, timestamp);
    }

    public record FieldError(
        @JsonProperty("field") String field,
        @JsonProperty("message") String message
    ) {}
}
