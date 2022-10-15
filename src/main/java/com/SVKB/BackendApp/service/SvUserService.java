package com.SVKB.BackendApp.service;

import com.SVKB.BackendApp.Auth.ApplicationUser;
import com.SVKB.BackendApp.DTOs.SvUserDTO;
import com.SVKB.BackendApp.model.ERole;
import com.SVKB.BackendApp.model.Role;
import com.SVKB.BackendApp.model.SvUser;
import com.SVKB.BackendApp.repo.RoleRepo;
import com.SVKB.BackendApp.repo.SvUserRepo;
import lombok.AllArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Locale;

@Service
@AllArgsConstructor
public class SvUserService{

    private SvUserRepo svUserRepo;

    private PasswordEncoder passwordEncoder;

    private RoleRepo roleRepo;




    public ResponseEntity<?> CreateUser(SvUserDTO svUserDTO){
        if(svUserRepo.existsByUsername(svUserDTO.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"username already exists!");
        }else{
            SvUser newSvUsers=mapfromDtoToModel(svUserDTO);
            svUserRepo.save(newSvUsers);

            return ResponseEntity.ok().body(ApplicationUser.build(newSvUsers));
        }
    }



    public SvUser mapfromDtoToModel(SvUserDTO svUserDTO){
        SvUser svUser=new SvUser();
        svUser.setName(svUserDTO.getName());
        svUser.setPassword(passwordEncoder.encode(svUserDTO.getPassword()));
        svUser.setUsername(svUserDTO.getUsername());
        if (svUserDTO.getRoles().toUpperCase(Locale.ROOT).equals("ROLE_IT_ADMIN")) {
            Role role =new Role();
            role.setName(ERole.ROLE_IT_ADMIN);
            svUser.setRoles(role);
            return svUser;
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Role not valid");
        }

    }
}
