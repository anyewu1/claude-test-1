package com.jd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jd.entity.Favorite;
import com.jd.mapper.FavoriteMapper;
import com.jd.service.FavoriteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

    @Override
    public List<Favorite> getUserFavorites(Long userId) {
        return baseMapper.findFavoritesWithProduct(userId);
    }

    @Override
    public void addFavorite(Long userId, Long productId) {
        if (baseMapper.countByUserAndProduct(userId, productId) > 0) {
            throw new RuntimeException("已在收藏夹中");
        }
        Favorite fav = new Favorite();
        fav.setUserId(userId);
        fav.setProductId(productId);
        save(fav);
    }

    @Override
    public void removeFavorite(Long userId, Long productId) {
        remove(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getProductId, productId));
    }

    @Override
    public boolean isFavorited(Long userId, Long productId) {
        return baseMapper.countByUserAndProduct(userId, productId) > 0;
    }
}
