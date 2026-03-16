package com.projects.system.urlshortener.controller;

import com.projects.system.urlshortener.dto.ShortRequestDTO;
import com.projects.system.urlshortener.dto.UrlCreateResponseDTO;
import com.projects.system.urlshortener.service.UrlMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/url")
public class ShortController {
    private final UrlMappingService urlMappingService;

    @Autowired
    public ShortController(UrlMappingService urlMappingService) {
        this.urlMappingService = urlMappingService;
    }

    @PostMapping("/create")
    public ResponseEntity<UrlCreateResponseDTO> createShortUrl(@RequestBody ShortRequestDTO shortRequestDTO) {
        UrlCreateResponseDTO result = urlMappingService.createShortUrl(shortRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/delete/{short_code}")
    public ResponseEntity<Void> delete(@PathVariable("short_code") String shortCode) {
        urlMappingService.deleteShortUrl(shortCode);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
