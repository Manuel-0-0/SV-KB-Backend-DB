package com.SVKB.BackendApp.controller;

import com.SVKB.BackendApp.DTOs.CategoryModelDto;
import com.SVKB.BackendApp.model.CategoryModel;
import com.SVKB.BackendApp.repo.CategoryRepo;
import com.SVKB.BackendApp.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/category")
public class CategoryController {

    private CategoryService categoryService;

//    private CategoryRepo categoryRepo;

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

//    @PutMapping(path = "/Update-C/{id}")
//    public ResponseEntity<?> UpdateNumberofArticles(@PathVariable Long id){
//        log.info(id.toString());
//        log.info(categoryRepo.findById(id).toString());
//        return ResponseEntity.ok(categoryService.UpdateArticle(id));
//    }

}
