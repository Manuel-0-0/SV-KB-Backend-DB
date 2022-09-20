package com.SVKB.BackendApp.service;

import com.SVKB.BackendApp.DTOs.ArticleModelDto;
import com.SVKB.BackendApp.model.ArticleModel;
import com.SVKB.BackendApp.model.CategoryModel;
import com.SVKB.BackendApp.repo.ArticleRepo;
import com.SVKB.BackendApp.repo.CategoryRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        if(categoryRepo.findById(articleModelDto.getCategoryId()).isPresent()){
            articleRepo.save(articleModel);
            categoryRepo.updateArticleNum(articleModelDto.getCategoryId());
            return ResponseEntity.ok(articleModel+"new Article added!");
        }else {
            return (ResponseEntity<?>) ResponseEntity.badRequest();
        }

    }

    public ResponseEntity<?> oneArticle(Long Id){
        if(articleRepo.existsById(Id)){
        return ResponseEntity.ok(articleRepo.findById(Id));
    }else{
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article can't be found.");
        }
    }

    public ResponseEntity<?> searchArticles(String keyword){
        List<ArticleModel> results= articleRepo.findBySearch(keyword);
        if(results==null){
            return ResponseEntity.ok("no results found!");
        }else {
            return ResponseEntity.ok(results);
        }
    }
    public ResponseEntity<?> DeleteArticle(Long Id){
        if(articleRepo.findById(Id).isPresent()){
            ArticleModel articleModel= articleRepo.findById(Id).get();
            categoryRepo.backdateArticleNum(articleModel.getCategoryArticles().getId());
            articleModel.setCategoryArticles(null);

            articleRepo.deleteById(Id);
            return ResponseEntity.ok("Deleted!");

        }else {
            return (ResponseEntity<?>) ResponseEntity.notFound();
        }
    }

    public ResponseEntity<?> ArticlesByCategories(Long CategoryId){
        List<ArticleModel> results= articleRepo.articlesByCategories(CategoryId);
        return ResponseEntity.status(HttpStatus.OK).body(results);
    }

    public ResponseEntity<?> UpdateArticle(Long Id, ArticleModelDto articleModelDto){
        if(articleRepo.existsById(Id)){
            ArticleModel articleModel =articleRepo.findById(Id).get();
            articleModel.setContent(articleModelDto.getContent());
            articleModel.setTitle(articleModelDto.getTitle());
            articleModel.setDateUpdated(LocalDateTime.now());
            articleRepo.save(articleModel);
            return ResponseEntity.status(200).body(articleRepo.findById(Id)+" Updated");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cant be found");
        }
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

}
