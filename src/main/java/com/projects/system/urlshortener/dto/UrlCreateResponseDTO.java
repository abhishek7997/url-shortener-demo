package com.projects.system.urlshortener.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record UrlCreateResponseDTO(
    @JsonProperty("short_code") String shortCode,
    @JsonProperty("long_url") String longUrl,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("expire_at") Instant expireAt
) {}
