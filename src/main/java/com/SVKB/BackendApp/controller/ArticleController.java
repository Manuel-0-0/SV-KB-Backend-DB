package com.SVKB.BackendApp.controller;

import com.SVKB.BackendApp.DTOs.ArticleModelDto;
import com.SVKB.BackendApp.model.ArticleModel;
import com.SVKB.BackendApp.repo.ArticleRepo;
import com.SVKB.BackendApp.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/articles")
public class ArticleController {

    @Autowired
    ArticleService articleService;
    @Autowired
    ArticleRepo articleRepo;

    @PostMapping(path = "/NewArticle")
    public ResponseEntity<?> CreateNewArticles(@RequestBody ArticleModelDto articleModelDto){
        articleService.createArticle(articleModelDto);
        return ResponseEntity.ok(articleModelDto.getTitle().toUpperCase(Locale.ROOT)+" Created");
    }

    @GetMapping(path = "/All")
    public ResponseEntity<?> AllArticles(){
        return articleService.AllArticles();
    }

    @DeleteMapping(path = "/Delete/{Id}")
    public ResponseEntity<?> DeleteArticle(@PathVariable Long Id){
        return articleService.DeleteArticle(Id);
    }

    @GetMapping(path="/Search")
    public ResponseEntity<?> SearchArticles(@RequestParam String keyword){
        return articleService.searchArticles(keyword);
    }

    @GetMapping(path = "/{Id}")
    public ResponseEntity<?> OneArticle(@PathVariable Long Id){
        return articleService.oneArticle(Id);
    }

    @GetMapping(path = "/")
    public ResponseEntity<?> SearchByCategories(@RequestParam Long CategoryId){
        return articleService.AllArticles();
    }

    @PutMapping(path ="/Update/{Id}")
    public ResponseEntity<?> UpdateArticles(@PathVariable Long Id, @RequestBody ArticleModelDto articleModelDto){
        return articleService.UpdateArticle(Id,articleModelDto);
    }

}
