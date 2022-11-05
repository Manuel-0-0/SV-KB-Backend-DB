package com.SVKB.BackendApp.repo;
import com.SVKB.BackendApp.model.ArticleModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ArticleRepo extends JpaRepository<ArticleModel,Long> {
//    @Transactional
//    @Query(
//            value = "SELECT * FROM tbl_article t WHERE t.title LIKE %?1%",
//            nativeQuery = true
//    )
//    List<ArticleModel> findByTitle( String title, PageRequest pageable, Sort sort);

    @Transactional
    @Query(value = "SELECT * FROM tbl_article p WHERE CONCAT(p.title,'') LIKE %?1% ",
    nativeQuery = true)
    List<ArticleModel> findBySearch(String keyword,PageRequest pageable);


    @Transactional
    @Query(value = "SELECT * FROM tbl_article p " +
            "WHERE p.title LIKE %?1% " +
            "AND p.draft_status=false",
            nativeQuery = true)
    List<ArticleModel> findBySearchPublished(String keyword,PageRequest pageable);



    @Transactional
    @Query(value = "SELECT * FROM tbl_article p WHERE Category_Id=:CategoryId",
    nativeQuery = true)
    List<ArticleModel> articlesByCategories(Long CategoryId, PageRequest pageable);

    @Query(value = "select * from tbl_article p",
            nativeQuery = true)
    List<ArticleModel> findAll(PageRequest pageable);

    @Transactional
    @Query(value = "SELECT * FROM tbl_article p WHERE draft_status='true'",
            nativeQuery = true)
    List<ArticleModel> findArticleModelByDraftStatusIsTrue(PageRequest pageable);

    @Transactional
    @Query(value = "SELECT * FROM tbl_article p WHERE draft_status='false'",
            nativeQuery = true)
    List<ArticleModel> findArticleModelByDraftStatusIsFalse(PageRequest pageable);

    @Transactional
    @Query(value = "SELECT * FROM tbl_article p WHERE sv_user_id=:Id ",
    nativeQuery = true)
    List<ArticleModel> findArticleModelByUser(Long Id, PageRequest pageable);
}