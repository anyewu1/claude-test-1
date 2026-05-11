package com.jd.controller;

import com.jd.common.Result;
import com.jd.common.UserContext;
import com.jd.service.BrowseHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class BrowseHistoryController {

    private final BrowseHistoryService browseHistoryService;

    @PostMapping("/{productId}")
    public Result<?> record(@PathVariable Long productId) {
        Long userId = UserContext.getUserId();
        if (userId != null) browseHistoryService.record(userId, productId);
        return Result.success(null);
    }

    @GetMapping
    public Result<?> recent(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(browseHistoryService.getRecent(UserContext.getUserId(), limit));
    }
}
