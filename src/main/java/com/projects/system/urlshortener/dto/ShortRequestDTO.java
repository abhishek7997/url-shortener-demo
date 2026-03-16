package com.projects.system.urlshortener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShortRequestDTO(
    @JsonProperty("long_url") String longUrl,
    @JsonProperty("custom_short_code") String customShortCode,
    @JsonProperty("expire_at") Instant expireAt
) {}
