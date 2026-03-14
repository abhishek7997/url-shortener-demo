package com.projects.system.urlshortener.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record UrlCreateResponseDTO(
    @JsonProperty("short_code") String shortCode,
    @JsonProperty("created_at") OffsetDateTime createdAt
) {}
