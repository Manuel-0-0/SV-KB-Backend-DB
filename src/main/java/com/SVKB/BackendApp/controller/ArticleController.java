package com.SVKB.BackendApp.controller;

import com.SVKB.BackendApp.DTOs.ArticleModelDto;
import com.SVKB.BackendApp.model.ArticleModel;
import com.SVKB.BackendApp.repo.ArticleRepo;
import com.SVKB.BackendApp.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        log.info(articleRepo.findByTitle(articleModelDto.getTitle()).toString());
        articleService.createArticle(articleModelDto);
        return ResponseEntity.ok(articleModelDto+"created");
    }

    @GetMapping(path = "/All")
    public ResponseEntity<?> AllArticles(){
        return ResponseEntity.ok(articleRepo.findAll());
    }


}
