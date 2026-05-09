package com.jd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.entity.Product;
import com.jd.mapper.ProductMapper;
import com.jd.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ObjectMapper objectMapper;

    @Override
    public Page<Product> search(int current, int size, Long categoryId, String keyword,
                                BigDecimal minPrice, BigDecimal maxPrice, String sortBy, String sortOrder) {
        Page<Product> page = new Page<>(current, size);
        Page<Product> result = baseMapper.searchProducts(page, categoryId, keyword, minPrice, maxPrice, sortBy, sortOrder);
        result.getRecords().forEach(this::parseImages);
        return result;
    }

    @Override
    public Product getDetail(Long id) {
        Product product = baseMapper.findByIdWithCategory(id);
        if (product != null) {
            parseImages(product);
        }
        return product;
    }

    @Override
    public List<Product> getFlashSale() {
        List<Product> products = list(new LambdaQueryWrapper<Product>()
                .eq(Product::getIsFlashSale, true)
                .eq(Product::getStatus, 1)
                .orderByDesc(Product::getSales)
                .last("LIMIT 8"));
        products.forEach(this::parseImages);
        return products;
    }

    @Override
    public List<Product> getRecommend(Long productId, int limit) {
        Product product = productId != null ? getById(productId) : null;
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<Product>()
                .eq(Product::getStatus, 1)
                .orderByDesc(Product::getRating)
                .last("LIMIT " + limit);
        if (product != null) {
            wrapper.eq(Product::getCategoryId, product.getCategoryId())
                   .ne(Product::getId, productId);
        }
        List<Product> products = list(wrapper);
        products.forEach(this::parseImages);
        return products;
    }

    @Override
    public List<Product> getByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        List<Product> products = listByIds(ids);
        products.forEach(this::parseImages);
        return products;
    }

    private void parseImages(Product product) {
        if (product.getImages() != null) {
            try {
                product.setImageList(objectMapper.readValue(product.getImages(), new TypeReference<List<String>>() {}));
            } catch (Exception e) {
                product.setImageList(List.of(product.getCoverImage()));
            }
        } else {
            product.setImageList(product.getCoverImage() != null ? List.of(product.getCoverImage()) : List.of());
        }
    }
}
