package com.projects.system.urlshortener.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
@Document(collection = "url_mapping")
public class UrlMapping {
    @Id
    private ObjectId id;
    @Field("short_code")
    @Indexed(unique = true)
    private String shortCode;
    @Field("long_url")
    private String longUrl;
    @Field("created_at")
    private Instant createdAt;
    @Field("expire_at")
    private Instant expireAt;
    @Field("hits")
    private long hits = 0;
}
