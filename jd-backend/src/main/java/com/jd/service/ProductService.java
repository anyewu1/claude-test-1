package com.jd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jd.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService extends IService<Product> {
    Page<Product> search(int current, int size, Long categoryId, String keyword,
                         BigDecimal minPrice, BigDecimal maxPrice, String sortBy, String sortOrder);
    Product getDetail(Long id);
    List<Product> getFlashSale();
    List<Product> getRecommend(Long productId, int limit);
    List<Product> getByIds(List<Long> ids);
}
