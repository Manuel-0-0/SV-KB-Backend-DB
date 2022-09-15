package com.SVKB.BackendApp.service;

import com.SVKB.BackendApp.DTOs.ArticleModelDto;
import com.SVKB.BackendApp.model.ArticleModel;
import com.SVKB.BackendApp.model.CategoryModel;
import com.SVKB.BackendApp.repo.ArticleRepo;
import com.SVKB.BackendApp.repo.CategoryRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Slf4j
@Service
public class ArticleService {
    @Autowired
    ArticleRepo articleRepo;
    @Autowired
    CategoryRepo categoryRepo;

    @Transactional
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
        CategoryModel categoryModelID=categoryRepo.findById(articleModelDto.getCategoryId()).orElse(null);

        newArticle.setCategoryArticles(categoryModelID);

        log.info("new article: "+newArticle);
        log.info("target category: "+ categoryRepo.findById(categoryModelID.getId()));
        log.info("stored category: "+ categoryModelID);
        log.info("new article category: " + newArticle.getCategoryArticles());
        return  newArticle;

    }
//

}
