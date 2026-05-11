package com.jd.controller;

import com.jd.common.Result;
import com.jd.dto.AIRecommendRequest;
import com.jd.dto.ChatRequest;
import com.jd.service.AIService;
import com.jd.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;
    private final EmbeddingService embeddingService;

    @PostMapping("/chat")
    public Result<?> chat(@RequestBody ChatRequest request) {
        return Result.success(aiService.chat(request));
    }

    @GetMapping("/suggest")
    public Result<?> suggest(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "5") int limit
    ) {
        return Result.success(aiService.suggest(keyword, limit));
    }

    @PostMapping("/recommend")
    public Result<?> recommend(@RequestBody AIRecommendRequest request) {
        return Result.success(aiService.recommend(request));
    }

    @PostMapping("/semantic-search")
    public Result<?> semanticSearch(@RequestBody Map<String, Object> body) {
        String query = (String) body.get("query");
        int limit = body.containsKey("limit") ? (int) body.get("limit") : 10;
        if (query == null || query.isBlank()) return Result.error(400, "搜索词不能为空");
        if (!embeddingService.isAvailable()) return Result.error(400, "向量搜索未启用，请配置 AI_EMBEDDING_MODEL");
        return Result.success(embeddingService.semanticSearch(query, limit));
    }

    @GetMapping("/embedding-status")
    public Result<?> embeddingStatus() {
        return Result.success(Map.of("available", embeddingService.isAvailable()));
    }
}
