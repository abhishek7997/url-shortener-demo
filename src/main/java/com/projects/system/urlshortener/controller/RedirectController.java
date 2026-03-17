package com.projects.system.urlshortener.controller;

import com.projects.system.urlshortener.entity.UrlMapping;
import com.projects.system.urlshortener.service.UrlMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/")
public class RedirectController {
    private final UrlMappingService urlMappingService;

    @Autowired
    public RedirectController(UrlMappingService urlMappingService) {
        this.urlMappingService = urlMappingService;
    }

    @GetMapping("/{short_code}")
    public ResponseEntity<Void> redirect(@PathVariable("short_code") String shortCode) {
        log.info("RedirectController:redirect shortCode={}", shortCode);

        UrlMapping urlMapping = urlMappingService.getLongUrlByShortCode(shortCode);

        log.info("RedirectController:redirect found record {}", urlMapping);

        return ResponseEntity
            .status(HttpStatus.FOUND)
            .location(URI.create(urlMapping.getLongUrl()))
            .build();
    }
}
