package com.SVKB.BackendApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;


    private Boolean DraftStatus;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "Img_Id")
    @JsonBackReference
    private Set<ImagesURL> imagesList;

    private String DateCreated;
    private String DateUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Category_Id")
    @JsonBackReference
    private CategoryModel categoryArticles;


}
