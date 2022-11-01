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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    //create new article
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


    //fine a single article by using Id
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


    //find all articles as a page x and y
    @Transactional
    public ResponseEntity<?> AllArticles(int pag, int NoContent, String order){
        List<ArticleModel> all = null;

        if(order.contains("asc")){
            PageRequest pageRequest = PageRequest.of(pag, NoContent);
            all=articleRepo.findAll(pageRequest,Sort.by("Id").ascending());
        }else if(order.contains("desc")){
            PageRequest pageRequest = PageRequest.of(pag, NoContent);
            all=articleRepo.findAll(pageRequest, Sort.by("Id").descending());
        }

//        Pageable pageable = (Pageable) PageRequest.of(page,size);


        return ResponseEntity.ok().body(PagedArticlesPlus(all));
    }


    //search for all articles using a keyword
    @Transactional
    public ResponseEntity<?> searchArticles(String keyword){
        List<ArticleModel> all= articleRepo.findBySearch(keyword);

        if(all==null) {
            return ResponseEntity.ok().body("no results found!");
        }
            return ResponseEntity.ok().body(RegArticlesPlus(all));
        }


    //delete one article by Id
    public ResponseEntity<?> DeleteArticle(Long Id){
        if(articleRepo.findById(Id).isPresent()){
            ArticleModel articleModel= articleRepo.findById(Id).get();
            categoryRepo.backdateArticleNum(articleModel.getCategory().getId());

            articleRepo.deleteById(Id);
            return ResponseEntity.ok("Deleted!");

        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no such article.");
        }
    }

    //find articles by based on category
    @Transactional
    public ResponseEntity<?> ArticlesByCategories(Long CategoryId){
        if(!categoryRepo.existsById(CategoryId)){
            return ResponseEntity.badRequest().body("Category not found.");
        }
        List<ArticleModel> results= articleRepo.articlesByCategories(CategoryId);
        return ResponseEntity.status(HttpStatus.OK).body(RegArticlesPlus(results));

    }


    //test method
    public ResponseEntity<?> testes(Long Id){
        List<ArticleModel> res=articleRepo.articlesByCategories(Id);
        return ResponseEntity.ok().body(res);
    }

    //find articles by based on the user that created it
    @Transactional
    public ResponseEntity<?> articleByUsr(Long id){
        if(svUserRepo.existsById(id)){
            List<ArticleModel> all= articleRepo.findArticleModelByUser(id);
            return ResponseEntity.ok().body(RegArticlesPlus(all));
        }else{
            return ResponseEntity.badRequest().body("No User Found");
        }
    }





    // find articles by based on draft status
    @Transactional
    public ResponseEntity<?> DraftStatus(String status){
        List<ArticleModel> allTrue=articleRepo.findArticleModelByDraftStatusIsTrue();
        List<ArticleModel> allFalse=articleRepo.findArticleModelByDraftStatusIsFalse();
        if(status.equalsIgnoreCase("false")){
            return ResponseEntity.ok().body(RegArticlesPlus(allTrue));
        }else if(status.equalsIgnoreCase("true")){
            return ResponseEntity.ok().body(RegArticlesPlus(allFalse));
        }else{
            return ResponseEntity.badRequest().body("Invalid DraftStatus");
        }
    }

    //Update an article using the id and dto
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



    //get all articles regularly
    private Object RegArticlesPlus(List<ArticleModel> all) {
        HashSet<Object> Articles= new HashSet<Object>();
        for (ArticleModel one:all) {
            Articles.add(ListMapper(one));
        }
        return Articles;
    }

    //get all articles as a page
    private Object PagedArticlesPlus(List<ArticleModel> all) {
        HashSet<Object> Articless= new HashSet<Object>();
        for (ArticleModel one:all) {
            Articless.add(ListMapper(one));
        }
        return Articless;
    }


    //object mapper for the article object
    private Map<String, Object> ListMapper(ArticleModel one) {
        Map<String, Object> map = new HashMap<String, Object>();
        CategoryModel artCategory = one.getCategory();
        SvUser user= one.getSvUser();

        map.put("Article", one);
        map.put("category_name", artCategory.getCategoryName());
        map.put("category_id", artCategory.getId());
        map.put("user_id",user.getUserId());
        map.put("user_name",user.getName());
        return map;
    }

    //map from dto to article model
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
