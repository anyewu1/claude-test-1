package com.jd.controller;

import com.jd.common.Result;
import com.jd.dto.AIRecommendRequest;
import com.jd.dto.ChatRequest;
import com.jd.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

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
}
