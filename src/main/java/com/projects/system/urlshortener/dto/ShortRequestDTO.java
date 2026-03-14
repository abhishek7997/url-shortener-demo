package com.projects.system.urlshortener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShortRequestDTO(@JsonProperty("long_url") String longUrl) {}
