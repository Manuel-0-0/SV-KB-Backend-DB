package com.SVKB.BackendApp.service;

import com.SVKB.BackendApp.DTOs.CategoryModelDto;
import com.SVKB.BackendApp.model.CategoryModel;
import com.SVKB.BackendApp.repo.CategoryRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryService {

    private CategoryRepo categoryRepo;


    public ResponseEntity<String> CreateCategory(CategoryModelDto categoryModelDto){
        if(categoryRepo.existsByCategoryName(categoryModelDto.getCategoryName())){
            return ResponseEntity.badRequest().body("Category already exists.");
        }
        CategoryModel categoryModel= MapFromDtoToCategoryModel(categoryModelDto);
        categoryRepo.save(categoryModel);
        return ResponseEntity.ok(categoryModel+"new Category added!");
    }
    public CategoryModel MapFromDtoToCategoryModel(CategoryModelDto categoryModelDto){
        CategoryModel NewCategory= new CategoryModel();
        NewCategory.setCategoryName(categoryModelDto.getCategoryName());

        return NewCategory;
    }
}
