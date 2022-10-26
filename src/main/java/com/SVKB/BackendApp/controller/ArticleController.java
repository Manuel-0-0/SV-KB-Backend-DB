package com.SVKB.BackendApp.controller;

import com.SVKB.BackendApp.DTOs.ArticleModelDto;
import com.SVKB.BackendApp.model.ArticleModel;
import com.SVKB.BackendApp.repo.ArticleRepo;
import com.SVKB.BackendApp.repo.SvUserRepo;
import com.SVKB.BackendApp.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/articles", produces="application/json")
public class ArticleController {

    @Autowired
    ArticleService articleService;
    @Autowired
    ArticleRepo articleRepo;

    @Autowired
    SvUserRepo svUserRepo;

    @PostMapping(path = "/NewArticle")
    public ResponseEntity<?> CreateNewArticles(@RequestBody ArticleModelDto articleModelDto){
        articleService.createArticle(articleModelDto);
        log.info(articleModelDto.toString());
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

    @GetMapping(path = "/" ,params = "CategoryId")
    public ResponseEntity<?> SearchByCategories(@RequestParam Long CategoryId){
        return articleService.ArticlesByCategories(CategoryId);
    }

    @GetMapping(path = "/test/")
    public ResponseEntity<?> test(@RequestParam Long id){
        return articleService.testes(id);
    }
    @PutMapping(path ="/Update/{Id}")
    public ResponseEntity<?> UpdateArticles(@PathVariable Long Id, @RequestBody ArticleModelDto articleModelDto){
        return articleService.UpdateArticle(Id,articleModelDto);
    }

    @GetMapping(path ="/user/{id}")
    public ResponseEntity<?> UserArticles(@PathVariable Long id){
        return articleService.articleByUsr(id);
    }

    @GetMapping(path="/", params = "draft_status")
    public ResponseEntity<?> draftStatus(@RequestParam String draft_status){
        return articleService.DraftStatus(draft_status);
    }
}
