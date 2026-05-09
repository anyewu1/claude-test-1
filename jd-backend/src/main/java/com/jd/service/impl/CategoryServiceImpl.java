package com.jd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jd.entity.Category;
import com.jd.mapper.CategoryMapper;
import com.jd.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<Category> getTree() {
        List<Category> all = list(new LambdaQueryWrapper<Category>().orderByAsc(Category::getSortOrder));
        List<Category> topLevel = all.stream().filter(c -> c.getParentId() == 0).collect(Collectors.toList());
        for (Category parent : topLevel) {
            List<Category> children = all.stream()
                    .filter(c -> parent.getId().equals(c.getParentId()))
                    .collect(Collectors.toList());
            parent.setChildren(children);
        }
        return topLevel;
    }

    @Override
    public List<Category> getTopLevel() {
        return list(new LambdaQueryWrapper<Category>()
                .eq(Category::getParentId, 0)
                .orderByAsc(Category::getSortOrder));
    }
}
