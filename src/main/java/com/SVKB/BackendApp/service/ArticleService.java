package com.SVKB.BackendApp.service;

import com.SVKB.BackendApp.DTOs.ArticleModelDto;
import com.SVKB.BackendApp.DTOs.CategoryModelDto;
import com.SVKB.BackendApp.model.ArticleModel;
import com.SVKB.BackendApp.model.CategoryModel;
import com.SVKB.BackendApp.repo.ArticleRepo;
import com.SVKB.BackendApp.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    ArticleRepo articleRepo;
    @Autowired
    CategoryRepo categoryRepo;


    public ResponseEntity<?> createArticle(ArticleModelDto articleModelDto){
        ArticleModel articleModel=MapFromDtoArticleModel(articleModelDto);
        articleRepo.save(articleModel);

        return ResponseEntity.ok(articleModel+"new Article added!");


    }

    public ArticleModel MapFromDtoArticleModel(ArticleModelDto articleModelDto){
        ArticleModel newArticle= new ArticleModel();
        newArticle.setContent(articleModelDto.getContent());
//        newArticle.setImagesList(articleModelDto.getImages());
        newArticle.setDateCreated(LocalDateTime.now());
        newArticle.setTitle(articleModelDto.getTitle());
        Long cId=        articleModelDto.getCategoryId();

        newArticle.setCategoryArticles(categoryRepo.findById(cId));


        return  newArticle;

    }
//

}
