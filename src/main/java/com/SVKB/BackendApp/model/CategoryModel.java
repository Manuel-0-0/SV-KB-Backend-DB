package com.SVKB.BackendApp.model;


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
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long Id;
    private String categoryName;
    private Integer articleNum;

    @OneToMany(mappedBy = "CategoryArticles")
    private Set<ArticleModel> CategoryArticles;


}
