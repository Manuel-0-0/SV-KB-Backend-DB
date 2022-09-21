package com.SVKB.BackendApp.Auth;

import com.SVKB.BackendApp.model.SvUser;
import com.SVKB.BackendApp.repo.SvUserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@AllArgsConstructor
@Service
public class ApplicationUserService implements UserDetailsService {

    private SvUserRepo svUserRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SvUser svUser= svUserRepo.findByUsername(username);
        return ApplicationUser.build(svUser);
    }
}
