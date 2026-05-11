package com.jd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.entity.Product;
import com.jd.entity.ProductEmbedding;
import com.jd.mapper.ProductEmbeddingMapper;
import com.jd.service.EmbeddingService;
import com.jd.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmbeddingServiceImpl implements EmbeddingService {

    private final ProductService productService;
    private final ProductEmbeddingMapper embeddingMapper;
    private final ObjectMapper objectMapper;

    @Autowired(required = false)
    private EmbeddingModel embeddingModel;

    public EmbeddingServiceImpl(ProductService productService,
                                ProductEmbeddingMapper embeddingMapper,
                                ObjectMapper objectMapper) {
        this.productService = productService;
        this.embeddingMapper = embeddingMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean isAvailable() {
        return embeddingModel != null;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        if (!isAvailable()) {
            log.info("EmbeddingModel not configured — semantic search disabled. Set OPENAI_API_KEY to enable.");
            return;
        }
        log.info("Generating product embeddings on startup...");
        generateAndStoreAll();
    }

    @Override
    public void generateAndStoreAll() {
        if (!isAvailable()) return;
        List<Product> products = productService.list(new LambdaQueryWrapper<Product>()
                .eq(Product::getStatus, 1));
        for (Product product : products) {
            try {
                generateAndStore(product);
            } catch (Exception e) {
                log.warn("Failed to generate embedding for product {}: {}", product.getId(), e.getMessage());
            }
        }
        log.info("Generated embeddings for {} products", products.size());
    }

    @Override
    public void generateAndStore(Product product) {
        if (!isAvailable()) return;
        String text = product.getName() + " " +
                (product.getDescription() != null ? product.getDescription() : "");
        float[] vector = embeddingModel.embed(text);

        try {
            String json = objectMapper.writeValueAsString(floatsToList(vector));
            ProductEmbedding pe = embeddingMapper.findByProductId(product.getId());
            if (pe == null) {
                pe = new ProductEmbedding();
                pe.setProductId(product.getId());
                pe.setEmbedding(json);
                pe.setUpdatedAt(LocalDateTime.now());
                embeddingMapper.insert(pe);
            } else {
                pe.setEmbedding(json);
                pe.setUpdatedAt(LocalDateTime.now());
                embeddingMapper.updateById(pe);
            }
        } catch (Exception e) {
            log.error("Failed to store embedding for product {}: {}", product.getId(), e.getMessage());
        }
    }

    @Override
    public List<Product> semanticSearch(String query, int limit) {
        if (!isAvailable()) return List.of();
        try {
            float[] queryVector = embeddingModel.embed(query);
            List<ProductEmbedding> all = embeddingMapper.selectList(null);
            if (all.isEmpty()) return List.of();

            List<long[]> scored = new ArrayList<>();
            for (ProductEmbedding pe : all) {
                float[] productVector = parseEmbedding(pe.getEmbedding());
                double similarity = cosineSimilarity(queryVector, productVector);
                // encode similarity as long (×1e9) for sorting without Comparator boxing
                scored.add(new long[]{ pe.getProductId(), (long)(similarity * 1_000_000_000) });
            }
            scored.sort((a, b) -> Long.compare(b[1], a[1]));

            List<Long> topIds = scored.stream()
                    .limit(limit)
                    .map(a -> a[0])
                    .collect(Collectors.toList());
            if (topIds.isEmpty()) return List.of();
            return productService.getByIds(topIds);
        } catch (Exception e) {
            log.error("Semantic search failed: {}", e.getMessage());
            return List.of();
        }
    }

    private double cosineSimilarity(float[] a, float[] b) {
        int len = Math.min(a.length, b.length);
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < len; i++) {
            dot += (double) a[i] * b[i];
            normA += (double) a[i] * a[i];
            normB += (double) b[i] * b[i];
        }
        double denom = Math.sqrt(normA) * Math.sqrt(normB);
        return denom < 1e-10 ? 0 : dot / denom;
    }

    private float[] parseEmbedding(String json) throws Exception {
        List<Double> list = objectMapper.readValue(json, new TypeReference<List<Double>>() {});
        float[] arr = new float[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i).floatValue();
        return arr;
    }

    private List<Float> floatsToList(float[] arr) {
        List<Float> list = new ArrayList<>(arr.length);
        for (float v : arr) list.add(v);
        return list;
    }
}
