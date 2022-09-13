package com.SVKB.BackendApp.repo;

import com.SVKB.BackendApp.model.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<CategoryModel,Long> {

    Boolean existsByCategoryName(String categoryName);

    @Modifying
    @Transactional
    @Query(
            value = "update tbl_category SET article_num= article_num + 1 WHERE Id=:Id",
            nativeQuery = true
    )
    void updateArticleNum(@Param("Id") Long Id);

//    List<CategoryModel> findAll();


    List<CategoryModel> findAll();

}
