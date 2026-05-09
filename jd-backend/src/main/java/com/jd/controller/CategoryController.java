package com.jd.controller;

import com.jd.common.Result;
import com.jd.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public Result<?> getTree() {
        return Result.success(categoryService.getTree());
    }

    @GetMapping("/top")
    public Result<?> getTopLevel() {
        return Result.success(categoryService.getTopLevel());
    }
}
