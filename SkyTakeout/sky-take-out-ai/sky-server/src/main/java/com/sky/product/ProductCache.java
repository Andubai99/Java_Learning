package com.sky.product;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Component
public class ProductCache {
    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> List<T> cachedList(String key, Supplier<List<T>> loader) {
        return (List<T>) cache.computeIfAbsent(key, ignored -> loader.get());
    }

    public void evictAll() {
        cache.clear();
    }

    public int size() {
        return cache.size();
    }
}
