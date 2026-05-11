package com.jd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jd.entity.Favorite;

import java.util.List;

public interface FavoriteService extends IService<Favorite> {
    List<Favorite> getUserFavorites(Long userId);
    void addFavorite(Long userId, Long productId);
    void removeFavorite(Long userId, Long productId);
    boolean isFavorited(Long userId, Long productId);
}
