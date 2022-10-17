package com.SVKB.BackendApp.controller;

import com.SVKB.BackendApp.Auth.ApplicationUser;
import com.SVKB.BackendApp.DTOs.LoginRequest;
import com.SVKB.BackendApp.DTOs.SvUserDTO;
import com.SVKB.BackendApp.service.SvUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(path = "/auth")
public class SvUserController {

    private SvUserService svUserService;


    @PostMapping(path = "/signup")
    public ResponseEntity<?> CreateUser(@RequestBody SvUserDTO svUserDto){
        log.info(String.valueOf(svUserDto));
        return svUserService.CreateUser(svUserDto);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> CreateUser(@RequestBody LoginRequest loginRequest){
        log.info(String.valueOf(loginRequest));
        return svUserService.loginSvUser(loginRequest);
    }
}
