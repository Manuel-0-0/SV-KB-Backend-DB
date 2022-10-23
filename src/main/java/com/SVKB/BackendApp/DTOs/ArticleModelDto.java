package com.SVKB.BackendApp.DTOs;


import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
public class ArticleModelDto{
    private final String title;
    private final String content;
    private final Long CategoryId;
    private final Boolean draftStatus;
}
