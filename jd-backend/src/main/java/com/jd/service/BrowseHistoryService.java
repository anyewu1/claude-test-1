package com.jd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jd.entity.BrowseHistory;

import java.util.List;

public interface BrowseHistoryService extends IService<BrowseHistory> {
    void record(Long userId, Long productId);
    List<BrowseHistory> getRecent(Long userId, int limit);
}
