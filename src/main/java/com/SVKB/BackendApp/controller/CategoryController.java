package com.SVKB.BackendApp.controller;

import com.SVKB.BackendApp.DTOs.CategoryModelDto;
import com.SVKB.BackendApp.model.CategoryModel;
import com.SVKB.BackendApp.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/category")
public class CategoryController {

    private CategoryService categoryService;

    @PostMapping(path="/create")
    public ResponseEntity<?> CreateCategory(@RequestBody CategoryModelDto categoryModelDto){
        log.info(String.valueOf(categoryModelDto));
        return categoryService.CreateCategory(categoryModelDto);

    }

    @GetMapping(path = "/AllCategories")
    public List<CategoryModel> AllPosts(){
        log.info(categoryService.getAllTheCategories().toString());
        return categoryService.getAllTheCategories();
    }
}
