package com.jd.service;

import com.jd.dto.AIRecommendRequest;
import com.jd.dto.ChatRequest;

import java.util.List;
import java.util.Map;

public interface AIService {
    Map<String, Object> chat(ChatRequest request);
    List<String> suggest(String keyword, int limit);
    Map<String, Object> recommend(AIRecommendRequest request);
}
