package com.projects.system.urlshortener.service;

import com.projects.system.urlshortener.dto.ShortRequestDTO;
import com.projects.system.urlshortener.dto.UrlCreateResponseDTO;
import com.projects.system.urlshortener.entity.UrlMapping;
import com.projects.system.urlshortener.exception.UrlExpiredException;
import com.projects.system.urlshortener.exception.UrlNotFoundException;
import com.projects.system.urlshortener.exception.UrlServiceException;
import com.projects.system.urlshortener.repository.UrlMappingRepository;
import com.projects.system.urlshortener.util.Base62Convertor;
import io.micrometer.common.util.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.Instant;
import java.time.OffsetDateTime;

@Service
public class UrlMappingService {
    private final UrlMappingRepository urlMappingRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public UrlMappingService(UrlMappingRepository urlMappingRepository, MongoTemplate mongoTemplate) {
        this.urlMappingRepository = urlMappingRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public UrlMapping getLongUrlByShortCode(String shortCode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("short_code").is(shortCode));

        Update update = new Update();
        update.inc("hits");

        UrlMapping urlMapping = mongoTemplate.findAndModify(query, update, UrlMapping.class);

        if (urlMapping == null) {
            throw new UrlNotFoundException("Invalid short code");
        }

        if (urlMapping.getExpireAt() != null && urlMapping.getExpireAt().isBefore(Instant.now())) {
            urlMappingRepository.deleteByShortCode(shortCode);
            throw new UrlExpiredException("Short code has expired");
        }

        return urlMapping;
    }

    public UrlCreateResponseDTO createShortUrl(ShortRequestDTO shortRequest) {
        UrlMapping urlMapping = new UrlMapping();

        String shortCode = shortRequest.customShortCode();
        if (StringUtils.isBlank(shortRequest.customShortCode())) {
            shortCode = Base62Convertor.convertToBase62(new BigInteger(new ObjectId().toHexString(), 16));
        }
        urlMapping.setShortCode(shortCode);
        urlMapping.setLongUrl(shortRequest.longUrl());
        urlMapping.setCreatedAt(Instant.now());
        urlMapping.setExpireAt(shortRequest.expireAt());

        UrlMapping saved;

        try {
            saved = urlMappingRepository.save(urlMapping);
        } catch (DuplicateKeyException e) {
            throw new UrlServiceException("Short code already in use");
        }

        return new UrlCreateResponseDTO(saved.getShortCode(), saved.getLongUrl(), saved.getCreatedAt(), saved.getExpireAt());
    }

    public void deleteShortUrl(String shortCode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("short_code").is(shortCode));

        mongoTemplate.remove(query, UrlMapping.class);
    }
}
