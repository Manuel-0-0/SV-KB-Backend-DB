package com.SVKB.BackendApp.service;

import com.SVKB.BackendApp.DTOs.ArticleModelDto;
import com.SVKB.BackendApp.model.ArticleModel;
import com.SVKB.BackendApp.model.CategoryModel;
import com.SVKB.BackendApp.model.Sorter;
import com.SVKB.BackendApp.model.SvUser;
import com.SVKB.BackendApp.repo.ArticleRepo;
import com.SVKB.BackendApp.repo.CategoryRepo;
import com.SVKB.BackendApp.repo.SvUserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
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

    private final ArticleRepo articleRepo;
    private final CategoryRepo categoryRepo;
    private final SvUserRepo svUserRepo;

    DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ArticleService(ArticleRepo articleRepo, CategoryRepo categoryRepo, SvUserRepo svUserRepo) {
        this.articleRepo = articleRepo;
        this.categoryRepo = categoryRepo;
        this.svUserRepo = svUserRepo;
    }

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



    //search for all articles using a keyword
    @Transactional
    public ResponseEntity<?> searchArticles(String keyword, int pag, int NoContent, String order){
        List<ArticleModel> all= articleRepo.findBySearch(keyword,sort(pag, NoContent,order));
        if(all==null) {
            return ResponseEntity.ok().body("no results found!");
        }
            return ResponseEntity.ok().body(PagedArticlesPlus(all));
        }

    @Transactional
    public ResponseEntity<?> searchPublishedArticles(String keyword, int pag, int NoContent, String order){
        List<ArticleModel> all= articleRepo.findBySearchPublished(keyword,sort(pag, NoContent,order));

        if(all==null) {
            return ResponseEntity.ok().body("no results found!");
        }
        return ResponseEntity.ok().body(PagedArticlesPlus(all));
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
    public ResponseEntity<?> ArticlesByCategories(Long CategoryId, int pag, int NoContent, String order){
        if(!categoryRepo.existsById(CategoryId)){
            return ResponseEntity.badRequest().body("Category not found.");
        }
        List<ArticleModel> results= articleRepo.articlesByCategories(CategoryId,sort(pag, NoContent,order));
        return ResponseEntity.status(HttpStatus.OK).body(PagedArticlesPlus(results));

    }


    //test method
//    public ResponseEntity<?> testes(Long Id,int pag, int NoContent, String order){
//        List<ArticleModel> res=articleRepo.articlesByCategories(Id);
//        return ResponseEntity.ok().body(res);
//    }

    //find articles by based on the user that created it
    @Transactional
    public ResponseEntity<?> articleByUsr(Long id,int pag, int NoContent, String order){
        if(svUserRepo.existsById(id)){
            List<ArticleModel> all= articleRepo.findArticleModelByUser(id,sort(pag, NoContent,order));
            return ResponseEntity.ok().body(PagedArticlesPlus(all));
        }else{
            return ResponseEntity.badRequest().body("No User Found");
        }
    }





    // find articles by based on draft status
    @Transactional
    public ResponseEntity<?> DraftStatus(String status,int pag, int NoContent, String order){
        List<ArticleModel> allTrue=articleRepo.findArticleModelByDraftStatusIsTrue(sort(pag, NoContent,order));
        List<ArticleModel> allFalse=articleRepo.findArticleModelByDraftStatusIsFalse(sort(pag, NoContent,order));
        if(status.equalsIgnoreCase("false")){
            return ResponseEntity.ok().body(PagedArticlesPlus(allTrue));
        }else if(status.equalsIgnoreCase("true")){
            return ResponseEntity.ok().body(PagedArticlesPlus(allFalse));
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
            articleModel.setDraftStatus(articleModelDto.getDraftStatus());
            articleModel.setDateUpdated(LocalDateTime.now().format(formatter));
            articleRepo.save(articleModel);
            return ResponseEntity.status(200).body(articleRepo.findById(Id)+" Updated");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cant be found");
        }
    }

    @Transactional
    public ResponseEntity<?> AllArticles(int pag, int NoContent, String order){
        List<ArticleModel> all = articleRepo.findAll(sort(pag, NoContent,order));
        return ResponseEntity.ok().body(PagedArticlesPlus(all));
    }

    //sorter
    public PageRequest sort(int pag, int NoContent, String order){
        //        Pageable pageable = (Pageable) PageRequest.of(page,size);
        Sorter sorter= new Sorter(pag,NoContent,ord(order));

        return PageRequest.of(sorter.getPag(), sorter.getNoContent(), sorter.getOrder());
    }

    //order
    public Sort  ord(String order){
        if(order.contains("asc")){

            return Sort.by(Sort.Direction.ASC, "Id");
        }
        return Sort.by(Sort.Direction.DESC, "Id");
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
