package com.SVKB.BackendApp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Set;

import static org.hibernate.annotations.CascadeType.ALL;

@AllArgsConstructor
@NoArgsConstructor
@Setter
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "svUser", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<ArticleModel> articles;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(ALL)
    private Role roles;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "userId = " + userId + ", " +
                "name = " + name + ", " +
                "username = " + username + ", " +
                "password = " + password + ")";
    }
}