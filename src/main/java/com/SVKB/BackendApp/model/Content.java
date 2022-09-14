package com.SVKB.BackendApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;


@NoArgsConstructor
@Data
public class Content {


    private String words;
    private List<ImagesURL> images;

    public Content(String words, List<ImagesURL> images) {
        this.words = words;
        this.images = images;
    }
}
