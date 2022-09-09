package com.SVKB.BackendApp.model;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CategoryModel {

    @Id
    private Long Id;
    private String categoryName;
    private Integer articleNum;
}
