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
    @Transactional
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

    @Query("select a from ArticleModel a")
    List<ArticleModel> findAll();

    @Transactional
    @Query(value = "SELECT * FROM tbl_article p WHERE draft_status='true'",
            nativeQuery = true)
    List<ArticleModel> findArticleModelByDraftStatusIsTrue();

    @Transactional
    @Query(value = "SELECT * FROM tbl_article p WHERE draft_status='false'",
            nativeQuery = true)
    List<ArticleModel> findArticleModelByDraftStatusIsFalse();

    @Transactional
    @Query(value = "SELECT * FROM tbl_article p WHERE sv_user_id=:Id",
    nativeQuery = true)
    List<ArticleModel> findArticleModelByUser(Long Id);
}