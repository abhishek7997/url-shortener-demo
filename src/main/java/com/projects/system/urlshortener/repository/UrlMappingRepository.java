package com.projects.system.urlshortener.repository;

import com.projects.system.urlshortener.entity.UrlMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlMappingRepository extends MongoRepository<UrlMapping, String> {
    Optional<UrlMapping> findByShortCode(String shortCode);
    void deleteByShortCode(String shortCode);
}
