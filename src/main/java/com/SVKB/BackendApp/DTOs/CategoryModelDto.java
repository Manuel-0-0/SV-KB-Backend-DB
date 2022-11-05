package com.SVKB.BackendApp.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Setter
@Getter
@AllArgsConstructor
public class CategoryModelDto{
    private final String CategoryName;
    private final Integer ArticleNum;
}
