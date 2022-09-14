package com.SVKB.BackendApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name = "tbl_article")
public class ArticleModel {

    @Id
    @SequenceGenerator(
            name = "articleIdGenerator",
            allocationSize = 1,
            sequenceName = "articleIdGenerator"
    )
    @GeneratedValue(
            generator = "articleIdGenerator",
            strategy= GenerationType.SEQUENCE)
    private Long Id;
    @Column(name = "title")
    private String title;
    private String content;
//    private List<String> imagesList;
    private LocalDateTime DateCreated;
    private LocalDateTime DateUpdated;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "Category_Id")
    @JsonBackReference
    private CategoryModel CategoryArticles;

    public void setCategoryArticles(Optional<CategoryModel> byId) {
    }
}
