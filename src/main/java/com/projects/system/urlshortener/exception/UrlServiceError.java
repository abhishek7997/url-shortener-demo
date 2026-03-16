package com.projects.system.urlshortener.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record UrlServiceError(
    @JsonProperty("correlation_id") String correlationId,
    @JsonProperty("error_code") String errorCode,
    @JsonProperty("error_message") String errorMessage,
    @JsonProperty("timestamp") OffsetDateTime timestamp
) {}
