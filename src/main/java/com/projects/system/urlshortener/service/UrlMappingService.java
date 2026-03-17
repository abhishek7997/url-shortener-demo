package com.projects.system.urlshortener.service;

import com.projects.system.urlshortener.dto.ShortRequestDTO;
import com.projects.system.urlshortener.dto.UrlCreateResponseDTO;
import com.projects.system.urlshortener.entity.UrlMapping;
import com.projects.system.urlshortener.exception.UrlExpiredException;
import com.projects.system.urlshortener.exception.UrlNotFoundException;
import com.projects.system.urlshortener.exception.UrlServiceException;
import com.projects.system.urlshortener.repository.UrlMappingRepository;
import com.projects.system.urlshortener.util.Base62Convertor;
import com.projects.system.urlshortener.util.UniqueIdGenerator;
import com.projects.system.urlshortener.util.UrlNormalizer;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;

@Slf4j
@Service
public class UrlMappingService {
    private final UrlMappingRepository urlMappingRepository;
    private final MongoTemplate mongoTemplate;
    private final UniqueIdGenerator idGenerator;

    @Autowired
    public UrlMappingService(UrlMappingRepository urlMappingRepository, MongoTemplate mongoTemplate) {
        this.urlMappingRepository = urlMappingRepository;
        this.mongoTemplate = mongoTemplate;
        this.idGenerator = new UniqueIdGenerator();
    }

    public UrlMapping getLongUrlByShortCode(String shortCode) {
        Query query = new Query().addCriteria(Criteria.where("short_code").is(shortCode));
        Update update = new Update().inc("hits");

        UrlMapping urlMapping = mongoTemplate.findAndModify(query, update, UrlMapping.class);

        if (urlMapping == null) {
            throw new UrlNotFoundException("Invalid short code");
        }

        if (urlMapping.getExpireAt() != null && urlMapping.getExpireAt().isBefore(Instant.now())) {
//            urlMappingRepository.deleteByShortCode(shortCode); // to be handled separately via CRON jobs or TTL indexes
            throw new UrlExpiredException("Short code has expired");
        }

        return urlMapping;
    }

    public UrlCreateResponseDTO createShortUrl(ShortRequestDTO shortRequest) {
        UrlMapping urlMapping = new UrlMapping();

        String shortCode = shortRequest.customShortCode();
        if (StringUtils.isBlank(shortRequest.customShortCode())) {
//            shortCode = Base62Convertor.convertToBase62(new BigInteger(new ObjectId().toHexString(), 16)); // this generates slightly larger strings since ObjectID from mongoDB is 96-bits
            shortCode = Base62Convertor.convertToBase62(idGenerator.get()); // use snowflake algorithm to generate unique id
        }
        urlMapping.setShortCode(shortCode);
        URL longUrl = UrlNormalizer.normalize(shortRequest.longUrl());
        log.debug("Normalized URL: {} with host: {}", longUrl, longUrl.getHost());
        if (longUrl.getHost().endsWith("localhost")) {
            throw new UrlServiceException("The target url is not allowed to be redirected to.");
        }
        urlMapping.setLongUrl(longUrl.toString());
        urlMapping.setCreatedAt(Instant.now());
        urlMapping.setExpireAt(shortRequest.expireAt());

        UrlMapping saved;

        try {
            saved = urlMappingRepository.save(urlMapping);
        } catch (DuplicateKeyException e) {
            throw new UrlServiceException("Short code already in use");
        }

        log.info("Saved {} with short code {}", saved.getLongUrl(), saved.getShortCode());

        return new UrlCreateResponseDTO(saved.getShortCode(), saved.getLongUrl(), saved.getCreatedAt(), saved.getExpireAt());
    }

    public void deleteShortUrl(String shortCode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("short_code").is(shortCode));

        mongoTemplate.remove(query, UrlMapping.class);
        log.info("Deleted short code: {}", shortCode);
    }
}
