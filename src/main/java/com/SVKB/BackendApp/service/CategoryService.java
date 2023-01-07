package com.SVKB.BackendApp.service;

import com.SVKB.BackendApp.DTOs.ArticleModelDto;
import com.SVKB.BackendApp.DTOs.CategoryModelDto;
import com.SVKB.BackendApp.model.ArticleModel;
import com.SVKB.BackendApp.model.CategoryModel;
import com.SVKB.BackendApp.model.Sorter;
import com.SVKB.BackendApp.repo.ArticleRepo;
import com.SVKB.BackendApp.repo.CategoryRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;
    private final ArticleRepo articleRepo;

    DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public CategoryService(CategoryRepo categoryRepo, ArticleRepo articleRepo) {
        this.categoryRepo = categoryRepo;
        this.articleRepo = articleRepo;
    }


    public ResponseEntity<String> CreateCategory(CategoryModelDto categoryModelDto){
        if(categoryRepo.existsByCategoryName(categoryModelDto.getCategoryName())){
            return ResponseEntity.badRequest().body("Category already exists.");
        }
        CategoryModel categoryModel= MapFromDtoToCategoryModel(categoryModelDto);
        categoryRepo.save(categoryModel);
        return ResponseEntity.ok(categoryModel.getCategoryName().toUpperCase(Locale.ROOT)+" Category added!");
    }

    public ResponseEntity<?> UpdateCategory(Long Id, CategoryModelDto categoryModelDto){
        if(categoryRepo.findById(Id).isPresent()){
            categoryRepo.UpdateCategoryName(Id, categoryModelDto.getCategoryName());
            categoryRepo.findById(Id).get().setDateUpdated(LocalDateTime.now().format(formatter));
            return ResponseEntity.ok().body(categoryModelDto.getCategoryName() + " updated");
        }else {
            return ResponseEntity.badRequest().body("Category not found!");
        }
        }

        public ResponseEntity<?> categoryByName(String name,int pag, int NoContent, String order){
        List<CategoryModel> categories = categoryRepo.findCategoryModelByCategoryName(name,sort(pag, NoContent,order));
        if(!(categories ==null)){
            return ResponseEntity.ok().body(getcategories(categories,NoContent,pag));
        }else{
            return ResponseEntity.ok("no categories found");
        }
        }


        public ResponseEntity<?> DeleteCategory(Long Id){
        List<ArticleModel> articles= articleRepo.articlesByCategories(Id, PageRequest.of(0,Integer.MAX_VALUE, Sort.unsorted()));
            for (ArticleModel article:articles) {
                article.setCategory(null);
                articleRepo.deleteById(article.getId());
            }
            categoryRepo.deleteById(Id);
            return ResponseEntity.ok().body("Deleted!");
        }


    @Transactional
    public ResponseEntity<?> getAllTheCategories(int pag, int NoContent, String order){

        List<CategoryModel> categories=categoryRepo.findAllCat(sort(pag, NoContent,order));
        log.info(categories.toString());
        return ResponseEntity.ok().body(getcategories(categories,NoContent,pag));
    }

    public CategoryModel MapFromDtoToCategoryModel(CategoryModelDto categoryModelDto){
        CategoryModel NewCategory= new CategoryModel();
        NewCategory.setCategoryName(categoryModelDto.getCategoryName());
        NewCategory.setArticleNum(0);
        NewCategory.setDateCreated(LocalDateTime.now().format(formatter));
        return NewCategory;
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
    private Object getcategories(List<CategoryModel> all,  int NoContent,int CurrentPage) {
        HashSet<Object> categories= new HashSet<Object>() ;
        Map<String, Object> response= new HashMap<>();


        Map<String, Object> pagination=new HashMap<>();
        categories.addAll(all);

        int NumOfPages=categoryRepo.findAll().size()/NoContent;
        pagination.put("limit",categories.size());
        pagination.put("num_of_pages",NumOfPages);
        pagination.put("current_page",CurrentPage);

        response.put("Pagination",pagination);
        response.put("Categories",categories);
        return response;
    }

}
