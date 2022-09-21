package com.SVKB.BackendApp.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}