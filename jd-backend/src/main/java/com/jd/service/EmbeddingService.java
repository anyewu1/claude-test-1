package com.jd.service;

import com.jd.entity.Product;

import java.util.List;

public interface EmbeddingService {
    void generateAndStoreAll();
    void generateAndStore(Product product);
    List<Product> semanticSearch(String query, int limit);
    boolean isAvailable();
}
