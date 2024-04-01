package com.libreforge.integration.security.oauth2.google.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class InMemoryRefreshTokenStorage {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryRefreshTokenStorage.class);

    private Cache<String, String> cache;

    @PostConstruct
    public void init() {
        cache = CacheBuilder.newBuilder()
//            .expireAfterWrite(2, TimeUnit.HOURS)
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .maximumSize(1000)
        .build();
    }

    public void put(String sessionId, String refreshToken) {
        cache.put(sessionId, refreshToken);
    }

    public String get(String sessionId) {
        return cache.getIfPresent(sessionId);
    }
}
