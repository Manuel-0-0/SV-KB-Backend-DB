package com.SVKB.BackendApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
public class Sorter {
    private int pag;
    private int noContent;
    private Sort order;

    public Sorter(Sorter sort) {
    }
}
