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

    private CategoryRepo categoryRepo;

    @PostMapping(path="/create")
    public ResponseEntity<?> CreateCategory(@RequestBody CategoryModelDto categoryModelDto){
        return categoryService.CreateCategory(categoryModelDto);

    }

    @GetMapping(path = "/AllCategories")
    public List<CategoryModel> AllPosts(){
        return categoryService.getAllTheCategories();
    }


    @GetMapping(path = "/{Id}")
    public ResponseEntity<?> oneCategory(@PathVariable Long Id){
        return ResponseEntity.ok(categoryRepo.findById(Id));

    }

    @PutMapping(path = "/Update/{Id}")
    public ResponseEntity<?> EditCategory(@PathVariable Long Id, @RequestBody CategoryModelDto categoryModelDto){
        categoryService.UpdateCategory(Id,categoryModelDto);
        return ResponseEntity.ok("updated!");
    }

    @DeleteMapping(path = "Delete/{Id}")
    public ResponseEntity<?> DeleteCategory(@PathVariable Long Id){
        return categoryService.DeleteCategory(Id);
    }

}
