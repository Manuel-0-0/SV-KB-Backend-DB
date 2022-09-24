package com.SVKB.BackendApp.DTOs;

import com.SVKB.BackendApp.model.Content;
import com.SVKB.BackendApp.model.ImagesURL;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Setter
@Getter
public class ArticleModelDto{
    private final String title;
    private final String content;
    private final Set<ImagesURL> images;
    private final Long CategoryId;
}
