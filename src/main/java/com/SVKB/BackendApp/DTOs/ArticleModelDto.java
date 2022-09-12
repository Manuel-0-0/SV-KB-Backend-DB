package com.SVKB.BackendApp.DTOs;

import com.SVKB.BackendApp.model.Content;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ArticleModelDto implements Serializable {
    private final String title;
    private final LocalDateTime DateCreated;
    private final Content content;
    private final LocalDateTime DateUpdated;
    private final CategoryModelDto categoryModel;
}
