package com.SVKB.BackendApp.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<ArticleModel> articles;
    
    
    public CategoryModel(String categoryName, Integer articleNum) {
        this.categoryName = categoryName;
        this.articleNum = articleNum;
    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "Id=" + Id +
                ", categoryName='" + categoryName + '\'' +
                ", articleNum=" + articleNum +
                ", articles=" + articles +
                '}';
    }
}
