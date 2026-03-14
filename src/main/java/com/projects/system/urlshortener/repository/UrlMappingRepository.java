package com.projects.system.urlshortener.repository;

import com.projects.system.urlshortener.entity.UrlMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlMappingRepository extends MongoRepository<UrlMapping, String> {
}
