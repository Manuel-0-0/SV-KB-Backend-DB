package com.SVKB.BackendApp.controller;

import com.SVKB.BackendApp.DTOs.ArticleModelDto;
import com.SVKB.BackendApp.repo.ArticleRepo;
import com.SVKB.BackendApp.repo.SvUserRepo;
import com.SVKB.BackendApp.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/articles", produces="application/json")
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleRepo articleRepo;
    private final SvUserRepo svUserRepo;

    public ArticleController(ArticleService articleService, ArticleRepo articleRepo, SvUserRepo svUserRepo) {
        this.articleService = articleService;
        this.articleRepo = articleRepo;
        this.svUserRepo = svUserRepo;
    }

    @PostMapping(path = "/NewArticle")
    public ResponseEntity<?> CreateNewArticles(@RequestBody ArticleModelDto articleModelDto){
        articleService.createArticle(articleModelDto);
        log.info(articleModelDto.toString());
        return ResponseEntity.ok(articleModelDto.getTitle().toUpperCase(Locale.ROOT)+" Created");
    }

    @GetMapping(path = "/All")
    public ResponseEntity<?> AllArticles(@RequestParam(defaultValue = "0")int page,
                                         @RequestParam(defaultValue = "10")int limit,
                                         @RequestParam(defaultValue = "desc") String order){
        return articleService.AllArticles(page, limit,order);
    }

    @DeleteMapping(path = "/Delete/{Id}")
    public ResponseEntity<?> DeleteArticle(@PathVariable Long Id){
        return articleService.DeleteArticle(Id);
    }

    @GetMapping(path="/Search")
    public ResponseEntity<?> SearchArticles(@RequestParam String keyword,
                                            @RequestParam(defaultValue = "0")int page,
                                            @RequestParam(defaultValue = "10")int limit,
                                            @RequestParam(defaultValue = "desc") String order){
        return articleService.searchArticles(keyword,page,limit,order);
    }

    @GetMapping(path="/Search/")
    public ResponseEntity<?> SearchPublishedArticles(@RequestParam String keyword,
                                            @RequestParam(defaultValue = "0")int page,
                                            @RequestParam(defaultValue = "10")int limit,
                                            @RequestParam(defaultValue = "desc") String order){
        return articleService.searchPublishedArticles(keyword,page,limit,order);
    }

    @GetMapping(path = "/All" , params = "draftStatus")
    public ResponseEntity<?> publishedArticles(@RequestParam String draftStatus,
                                               @RequestParam(defaultValue = "0")int page,
                                               @RequestParam(defaultValue = "10")int limit,
                                               @RequestParam(defaultValue = "desc") String order){
        return  articleService.DraftStatus(draftStatus, page, limit,order);
    }

    @GetMapping(path = "/{Id}")
    public ResponseEntity<?> OneArticle(@PathVariable Long Id){
        return articleService.oneArticle(Id);
    }

    @GetMapping(path = "/" ,params = "CategoryId")
    public ResponseEntity<?> SearchByCategories(@RequestParam Long CategoryId,
                                                @RequestParam(defaultValue = "0")int page,
                                                @RequestParam(defaultValue = "10")int limit,
                                                @RequestParam(defaultValue = "desc") String order){
        return articleService.ArticlesByCategories(CategoryId,page,limit,order);
    }

    @GetMapping(path = "/published/" ,params = "CategoryId")
    public ResponseEntity<?> SearchByPublishedCategories(@RequestParam Long CategoryId,
                                                @RequestParam(defaultValue = "0")int page,
                                                @RequestParam(defaultValue = "10")int limit,
                                                @RequestParam(defaultValue = "desc") String order){
        return articleService.PublishedArticlesByCategories(CategoryId,page,limit,order);
    }



//    @GetMapping(path = "/test/")
//    public ResponseEntity<?> test(@RequestParam Long id){
//        return articleService.testes(id);
//    }
    @PutMapping(path ="/Update/{Id}")
    public ResponseEntity<?> UpdateArticles(@PathVariable Long Id, @RequestBody ArticleModelDto articleModelDto){
        return articleService.UpdateArticle(Id,articleModelDto);
    }

    @GetMapping(path ="/user/{id}")
    public ResponseEntity<?> UserArticles(@PathVariable Long id,
                                          @RequestParam(defaultValue = "0")int page,
                                          @RequestParam(defaultValue = "10")int limit,
                                          @RequestParam(defaultValue = "desc") String order){
        return articleService.articleByUsr(id,page,limit,order);
    }

//    @GetMapping(path="/", params = "draft_status")
//    public ResponseEntity<?> draftStatus(@RequestParam String draft_status){
//        return articleService.DraftStatus(draft_status);
//    }
}
