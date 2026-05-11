package com.jd.controller;

import com.jd.common.Result;
import com.jd.common.UserContext;
import com.jd.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public Result<?> list() {
        return Result.success(favoriteService.getUserFavorites(UserContext.getUserId()));
    }

    @PostMapping("/{productId}")
    public Result<?> add(@PathVariable Long productId) {
        favoriteService.addFavorite(UserContext.getUserId(), productId);
        return Result.success("已收藏", null);
    }

    @DeleteMapping("/{productId}")
    public Result<?> remove(@PathVariable Long productId) {
        favoriteService.removeFavorite(UserContext.getUserId(), productId);
        return Result.success("已取消收藏", null);
    }

    @GetMapping("/check/{productId}")
    public Result<?> check(@PathVariable Long productId) {
        return Result.success(favoriteService.isFavorited(UserContext.getUserId(), productId));
    }
}
