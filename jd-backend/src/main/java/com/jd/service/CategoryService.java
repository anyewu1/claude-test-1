package com.jd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jd.entity.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {
    List<Category> getTree();
    List<Category> getTopLevel();
}
