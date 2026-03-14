package com.projects.system.urlshortener.controller;

import com.projects.system.urlshortener.dto.ShortRequestDTO;
import com.projects.system.urlshortener.dto.UrlCreateResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/url")
public class ShortController {
    @PostMapping("/create")
    public ResponseEntity<UrlCreateResponseDTO> createShortUrl(@RequestBody ShortRequestDTO shortRequestDTO) {
        log.info("REQUEST: {}", shortRequestDTO);
        return null;
    }
}
