package com.SVKB.BackendApp.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ArticleModel {

    @Id
    private Long Id;
    private String title;
    @Transient
    private Content content;
    private LocalDateTime DateCreated;
    private LocalDateTime DateUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id")
    private CategoryModel CategoryArticles;
}
