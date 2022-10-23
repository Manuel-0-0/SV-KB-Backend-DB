package com.SVKB.BackendApp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static org.hibernate.annotations.CascadeType.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@Getter
@Entity
public class SvUser {

    @Id
    @SequenceGenerator(
            name = "UserIdGenerator",
            allocationSize = 1,
            sequenceName = "UserIdGenerator"
    )
    @GeneratedValue(
            generator = "UserIdGenerator",
            strategy= GenerationType.SEQUENCE)
    private Long userId;
    private String name;
    private String username;
    private String password;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "svUser", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<ArticleModel> articles;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(ALL)
    private Role roles;
}