package com.SVKB.BackendApp.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    private String DateCreated;

    private String DateUpdated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @JsonIgnore
    private Set<ArticleModel> articles;
    
    
    public CategoryModel(String categoryName, Integer articleNum, String DateCreated,String DateUpdated) {
        this.categoryName = categoryName;
        this.articleNum = articleNum;
        this.DateCreated=DateCreated;
        this.DateUpdated=DateUpdated;
    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "Id=" + Id +
                ", categoryName='" + categoryName + '\'' +
                ", articleNum=" + articleNum +
                ", DateCreated="+ DateCreated+
                ", DateUpdated="+ DateUpdated+
                '}';
    }
}
