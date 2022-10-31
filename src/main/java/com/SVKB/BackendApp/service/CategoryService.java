package com.SVKB.BackendApp.service;

import com.SVKB.BackendApp.DTOs.CategoryModelDto;
import com.SVKB.BackendApp.model.ArticleModel;
import com.SVKB.BackendApp.model.CategoryModel;
import com.SVKB.BackendApp.model.SvUser;
import com.SVKB.BackendApp.repo.ArticleRepo;
import com.SVKB.BackendApp.repo.CategoryRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@AllArgsConstructor
public class CategoryService {

    private CategoryRepo categoryRepo;
    private ArticleRepo articleRepo;


    public ResponseEntity<String> CreateCategory(CategoryModelDto categoryModelDto){
        if(categoryRepo.existsByCategoryName(categoryModelDto.getCategoryName())){
            return ResponseEntity.badRequest().body("Category already exists.");
        }
        CategoryModel categoryModel= MapFromDtoToCategoryModel(categoryModelDto);
        categoryRepo.save(categoryModel);
        return ResponseEntity.ok(categoryModel.getCategoryName().toUpperCase(Locale.ROOT)+" Category added!");
    }

    public ResponseEntity<?> UpdateCategory(Long Id, CategoryModelDto categoryModelDto){
        if(categoryRepo.findById(Id).isPresent()){
            categoryRepo.UpdateCategoryName(Id, categoryModelDto.getCategoryName());
            return ResponseEntity.ok(categoryRepo.findById(Id) + "new Category added!");
        }else {
            return ResponseEntity.badRequest().body("Category not found!");
        }
        }

        public ResponseEntity<?> categoryByName(String name){
        List<CategoryModel> categories = categoryRepo.findCategoryModelByCategoryName(name);
        if(!(categories ==null)){
            return ResponseEntity.ok().body(getcategories(categories));
        }else{
            return ResponseEntity.ok("no categories found");
        }
        }


        public ResponseEntity<?> DeleteCategory(Long Id){
        List<ArticleModel> articles= articleRepo.articlesByCategories(Id);
            for (ArticleModel article:articles) {
                article.setCategory(null);
                articleRepo.deleteById(article.getId());
            }
            categoryRepo.deleteById(Id);
            return ResponseEntity.ok().body("Deleted!");
        }


    @Transactional
    public ResponseEntity<?> getAllTheCategories(){
        List<CategoryModel> categories=categoryRepo.findAllCat();
        return ResponseEntity.ok().body(getcategories(categories));
    }

    public CategoryModel MapFromDtoToCategoryModel(CategoryModelDto categoryModelDto){
        CategoryModel NewCategory= new CategoryModel();
        NewCategory.setCategoryName(categoryModelDto.getCategoryName());
        NewCategory.setArticleNum(0);

        return NewCategory;
    }


    private Object getcategories(List<CategoryModel> all) {
        HashSet<Object> categories= new HashSet<Object>() ;
        categories.addAll(all);
        return categories;
    }

}
