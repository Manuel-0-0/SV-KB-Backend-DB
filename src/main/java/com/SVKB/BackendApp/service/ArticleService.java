package com.SVKB.BackendApp.service;

import com.SVKB.BackendApp.DTOs.ArticleModelDto;
import com.SVKB.BackendApp.model.ArticleModel;
import com.SVKB.BackendApp.model.CategoryModel;
import com.SVKB.BackendApp.model.SvUser;
import com.SVKB.BackendApp.repo.ArticleRepo;
import com.SVKB.BackendApp.repo.CategoryRepo;
import com.SVKB.BackendApp.repo.SvUserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class ArticleService {
    @Autowired
    ArticleRepo articleRepo;
    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    private SvUserRepo svUserRepo;
    DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Transactional
    public ResponseEntity<?> createArticle(ArticleModelDto articleModelDto){
        ArticleModel articleModel=MapFromDtoArticleModel(articleModelDto);
        if(categoryRepo.findById(articleModelDto.getCategoryId()).isPresent()){
            articleRepo.save(articleModel);
            categoryRepo.updateArticleNum(articleModelDto.getCategoryId());
            return ResponseEntity.ok(articleModel+"new Article added!");
        }else {
            return ResponseEntity.badRequest().body("something off");
        }

    }

    public ResponseEntity<?> oneArticle(Long Id){
        if(articleRepo.existsById(Id)){
            ArticleModel articleModel=articleRepo.findById(Id).get();

            CategoryModel artCategory = articleModel.getCategory();
            SvUser user= articleModel.getSvUser();

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Article", articleModel);
            map.put("category_name", artCategory.getCategoryName());
            map.put("category_id", artCategory.getId());
            map.put("user_id",user.getUserId());
            map.put("user_name",user.getName());
        return ResponseEntity.ok(map);
    }else{
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article can't be found.");
        }
    }

    public ResponseEntity<?> AllArticles(){
//        Pageable pageable = (Pageable) PageRequest.of(page,size);
        List<ArticleModel> all=articleRepo.findAll();

        HashSet<Object> Articles= new HashSet<Object>() ;
        for (ArticleModel one:all) {
            Map<String, Object> map = new HashMap<String, Object>();
            CategoryModel artCategory = one.getCategory();
            SvUser user= one.getSvUser();

            map.put("Article", one);
            map.put("category_name", artCategory.getCategoryName());
            map.put("category_id", artCategory.getId());
            map.put("user_id",user.getUserId());
            map.put("user_name",user.getName());
            Articles.add(map);
        }

        return ResponseEntity.ok().body(Articles);
    }
    public ResponseEntity<?> searchArticles(String keyword){
        List<ArticleModel> results= articleRepo.findBySearch(keyword);
        if(results==null){
            return ResponseEntity.ok().body("no results found!");
        }else {
            return ResponseEntity.ok().body(results);
        }
    }
    public ResponseEntity<?> DeleteArticle(Long Id){
        if(articleRepo.findById(Id).isPresent()){
            ArticleModel articleModel= articleRepo.findById(Id).get();
            categoryRepo.backdateArticleNum(articleModel.getCategory().getId());
            articleModel.setCategory(null);

            articleRepo.deleteById(Id);
            return ResponseEntity.ok("Deleted!");

        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no such article.");
        }
    }

    @Transactional
    public ResponseEntity<?> ArticlesByCategories(Long CategoryId){
        List<ArticleModel> results= articleRepo.articlesByCategories(CategoryId);
        return ResponseEntity.status(HttpStatus.OK).body(results.toArray());
    }

    public ResponseEntity<?> testes(Long Id){
        List<ArticleModel> res=articleRepo.articlesByCategories(Id);
        return ResponseEntity.ok().body(res);
    }

    public ResponseEntity<?> UpdateArticle(Long Id, ArticleModelDto articleModelDto){
        if(articleRepo.existsById(Id)){
            ArticleModel articleModel =articleRepo.findById(Id).get();
            articleModel.setContent(articleModelDto.getContent());
            articleModel.setTitle(articleModelDto.getTitle());
            articleModel.setDateUpdated(LocalDateTime.now().format(formatter));
            articleRepo.save(articleModel);
            return ResponseEntity.status(200).body(articleRepo.findById(Id)+" Updated");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cant be found");
        }
    }


    public ArticleModel MapFromDtoArticleModel(ArticleModelDto articleModelDto){
        ArticleModel newArticle= new ArticleModel();
        newArticle.setContent(articleModelDto.getContent());
        newArticle.setDraftStatus(articleModelDto.getDraftStatus());
        newArticle.setDateCreated(LocalDateTime.now().format(formatter));

        newArticle.setTitle(articleModelDto.getTitle());
        CategoryModel categoryModelID=categoryRepo.findById(articleModelDto.getCategoryId()).orElse(null);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String usrname=auth.getName();
        log.info("username :"+ usrname);
        SvUser svuser=svUserRepo.findByUsername(usrname);
        log.info("svuser :" +svuser.toString());
        newArticle.setCategory(categoryModelID);
        newArticle.setSvUser(svuser);
        log.info("new article: "+newArticle);


        return  newArticle;

    }



}
