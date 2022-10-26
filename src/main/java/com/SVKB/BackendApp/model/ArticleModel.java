package com.SVKB.BackendApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    private String DateCreated;

    private String DateUpdated;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Category_Id", nullable = false)
    @JsonBackReference
    private CategoryModel category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SvUser_Id")
    @JsonIgnore
    private SvUser svUser;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "Id = " + Id + ", " +
                "title = " + title + ", " +
                "content = " + content + ", " +
                "DraftStatus = " + DraftStatus + ", " +
                "DateCreated = " + DateCreated + ", " +
                "DateUpdated = " + DateUpdated + ")";
    }
}
