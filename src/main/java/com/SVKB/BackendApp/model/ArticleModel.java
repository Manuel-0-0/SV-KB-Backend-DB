package com.SVKB.BackendApp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ArticleModel {

    @Id
    private Long Id;
    private String title;
    @Transient
    private Content content;
    private LocalDateTime DateCreated;
    private LocalDateTime DateUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private CategoryModel categoryModel;
}
