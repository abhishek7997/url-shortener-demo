package com.projects.system.urlshortener.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.OffsetDateTime;

@Document("url_mapping")
public class UrlMapping {
    @Id
    private String id;
    @Field("short_code")
    private String shortCode;
    @Field("long_url")
    private String longUrl;
    @Field("created_at")
    private OffsetDateTime createdAt;
    @Field("hits")
    private Long hits;
}
