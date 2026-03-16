package com.projects.system.urlshortener.exception;

public class UrlExpiredException extends RuntimeException {
    public UrlExpiredException(String message) {
        super(message);
    }
}
