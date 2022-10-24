package com.SVKB.BackendApp.repo;
import com.SVKB.BackendApp.model.ArticleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ArticleRepo extends JpaRepository<ArticleModel,Long> {

    @Query(
            value = "SELECT * FROM tbl_article t WHERE t.title LIKE %?1%",
            nativeQuery = true
    )
    List<ArticleModel> findByTitle( String title);

    @Query(value = "SELECT * FROM tbl_article p WHERE CONCAT(p.title,'') LIKE %?1%",
    nativeQuery = true)
    List<ArticleModel> findBySearch(String keyword);

    @Transactional
    @Query(value = "SELECT * FROM tbl_article p WHERE Category_Id=:CategoryId",
    nativeQuery = true)
    List<ArticleModel> articlesByCategories(Long CategoryId);

    List<ArticleModel> findAll();

}