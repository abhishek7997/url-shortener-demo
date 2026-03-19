package com.projects.system.urlshortener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShortRequestDTO(
    @URL
    @NotNull
    @JsonProperty("long_url") String longUrl,

    @Size(max = 12, message = "custom short code can be at max 12 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "custom short code contains disallowed characters")
    @JsonProperty("custom_short_code") String customShortCode,

    @Future(message = "expiration time must be after present time")
    @JsonProperty("expire_at") Instant expireAt
) {}
