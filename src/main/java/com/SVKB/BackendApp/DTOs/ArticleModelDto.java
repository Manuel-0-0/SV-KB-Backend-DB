package com.SVKB.BackendApp.DTOs;

import com.SVKB.BackendApp.model.Content;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Setter
@Getter
public class ArticleModelDto{
    private final String title;
    private final String content;
//    private final List<String> images;
    private final Long CategoryId;
}
