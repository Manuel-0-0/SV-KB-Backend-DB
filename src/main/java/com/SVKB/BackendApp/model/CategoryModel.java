package com.SVKB.BackendApp.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Optional;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(
        name = "tbl_category"
)
public class CategoryModel {


    @Id
    @SequenceGenerator(
            name = "categoryIdGenerator",
            allocationSize = 1,
            sequenceName = "categoryIdGenerator"
    )
    @GeneratedValue(
            generator = "categoryIdGenerator",
            strategy= GenerationType.SEQUENCE)
    private Long Id;
    private String categoryName;
    private Integer articleNum;

    @OneToMany(mappedBy = "CategoryArticles",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<ArticleModel> CategoryArticles;


    public CategoryModel(String categoryName, Integer articleNum, Set<ArticleModel> categoryArticles) {
        this.categoryName = categoryName;
        this.articleNum = articleNum;
        CategoryArticles = categoryArticles;
    }
}
