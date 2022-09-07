package com.SVKB.BackendApp.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SvUser {

    @Id
    private Long id;
    private String Name;
    private String username;
    private String password;
}
