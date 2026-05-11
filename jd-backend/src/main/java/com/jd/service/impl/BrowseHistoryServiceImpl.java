package com.jd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jd.entity.BrowseHistory;
import com.jd.mapper.BrowseHistoryMapper;
import com.jd.service.BrowseHistoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BrowseHistoryServiceImpl extends ServiceImpl<BrowseHistoryMapper, BrowseHistory> implements BrowseHistoryService {

    @Override
    public void record(Long userId, Long productId) {
        // Remove existing entry for this product (deduplicate, then re-insert as latest)
        remove(new LambdaQueryWrapper<BrowseHistory>()
                .eq(BrowseHistory::getUserId, userId)
                .eq(BrowseHistory::getProductId, productId));
        BrowseHistory history = new BrowseHistory();
        history.setUserId(userId);
        history.setProductId(productId);
        history.setViewedAt(LocalDateTime.now());
        save(history);
    }

    @Override
    public List<BrowseHistory> getRecent(Long userId, int limit) {
        return baseMapper.findRecentByUser(userId, limit);
    }
}
