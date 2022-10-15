package com.SVKB.BackendApp.Auth;

import com.SVKB.BackendApp.model.SvUser;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class ApplicationUser implements UserDetails {



    private Long Id;
    private final String username;
    private final String password;
    private final Collection<? extends  GrantedAuthority> authorities;

    public ApplicationUser(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        Id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static ApplicationUser build(SvUser svUser){
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(svUser.getRoles().getName().name()));
        return new ApplicationUser(
                svUser.getUserId(),
                svUser.getUsername(),
                svUser.getPassword(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return Id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
