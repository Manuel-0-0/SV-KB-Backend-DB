package com.SVKB.BackendApp.repo;
import com.SVKB.BackendApp.model.ArticleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepo extends JpaRepository<ArticleModel,Long> {

    @Query(
            value = "SELECT * FROM tbl_article t WHERE t.title LIKE %?1%",
            nativeQuery = true
    )
    List<ArticleModel> findByTitle( String title);

    boolean findByCategoryArticles(Long Id);
}