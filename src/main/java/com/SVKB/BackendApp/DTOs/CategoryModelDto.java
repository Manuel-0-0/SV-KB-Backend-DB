package com.SVKB.BackendApp.DTOs;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryModelDto implements Serializable {
    private final String CategoryName;
    private final Integer ArticleNum;
}
