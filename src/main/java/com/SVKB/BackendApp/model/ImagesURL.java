package com.SVKB.BackendApp.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
public class ImagesURL {

    @Id
    @SequenceGenerator(
            name = "ImgIdGenerator",
            allocationSize = 1,
            sequenceName = "ImgIdGenerator"
    )
    @GeneratedValue(
            generator = "ImgIdGenerator",
            strategy= GenerationType.SEQUENCE)
    private Long Id;
    private String ImgURL;
}
