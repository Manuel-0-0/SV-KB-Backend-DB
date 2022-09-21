package com.SVKB.BackendApp.service;

import com.SVKB.BackendApp.DTOs.SvUserDTO;
import com.SVKB.BackendApp.model.SvUser;
import com.SVKB.BackendApp.repo.SvUserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SvUserService {

    private SvUserRepo svUserRepo;

    private PasswordEncoder passwordEncoder;




//    mapfromDtoToModel(SvUserDTO svUserDTO){
//        SvUser svUser=new SvUser();
//        svUser.setName(svUserDTO.getName());
//        svUser.setPassword(passwordEncoder.encode(svUserDTO.getPassword()));
//        svUser.setUsername(svUserDTO.getUsername());
//    }
}
