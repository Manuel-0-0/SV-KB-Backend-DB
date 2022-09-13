package com.SVKB.BackendApp.service;

import com.SVKB.BackendApp.DTOs.ArticleModelDto;
import com.SVKB.BackendApp.DTOs.CategoryModelDto;
import com.SVKB.BackendApp.model.ArticleModel;
import com.SVKB.BackendApp.model.CategoryModel;
import com.SVKB.BackendApp.repo.ArticleRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ArticleService {
    ArticleRepo articleRepo;


    public ResponseEntity<?> createArticle(ArticleModelDto articleModelDto){
        ArticleModel articleModel=MapFromDtoToCategoryModel(articleModelDto);
        articleRepo.save(articleModel);

        return ResponseEntity.ok(articleModel+"new Article added!");


    }

    public ArticleModel MapFromDtoToCategoryModel(ArticleModelDto articleModelDto){
        ArticleModel newArticle= new ArticleModel();
        newArticle.setContent(articleModelDto.getContent());
        newArticle.setDateCreated(LocalDateTime.now());
        newArticle.setTitle(articleModelDto.getTitle());
//        CategoryModel categoryModel =new CategoryModel(articleRepo.findById(articleModelDto.getCategoryId()));


        return  newArticle;

    }
//

}
