package com.SVKB.BackendApp.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryModel {

    @Id
    private Long Id;
    private String CategoryName;
    private Integer ArticleNum;
}
