package com.SVKB.BackendApp.service;

import com.SVKB.BackendApp.Auth.ApplicationUser;
import com.SVKB.BackendApp.DTOs.LoginRequest;
import com.SVKB.BackendApp.DTOs.SvUserDTO;
import com.SVKB.BackendApp.Security.JwtUtils;
import com.SVKB.BackendApp.model.ERole;
import com.SVKB.BackendApp.model.Role;
import com.SVKB.BackendApp.model.SvUser;
import com.SVKB.BackendApp.repo.RoleRepo;
import com.SVKB.BackendApp.repo.SvUserRepo;
import lombok.AllArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@AllArgsConstructor
public class SvUserService{

    private SvUserRepo svUserRepo;
    private PasswordEncoder passwordEncoder;
    private RoleRepo roleRepo;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;


    public ResponseEntity<?> CreateUser(SvUserDTO svUserDTO){
        if(svUserRepo.existsByUsername(svUserDTO.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"username already exists!");
        }else{
            SvUser newSvUsers=mapfromDtoToModel(svUserDTO);
            svUserRepo.save(newSvUsers);

            return ResponseEntity.ok().body(ApplicationUser.build(newSvUsers));
        }
    }

    public ResponseEntity<?> loginSvUser(LoginRequest loginRequest) {

        if(loginRequest.getUsername().equalsIgnoreCase("administrator")){
            loginRequest.setUsername(loginRequest.getUsername().toLowerCase(Locale.ROOT));
        }
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            if(!authentication.isAuthenticated()) {
//                throw new BadCredentialsException("Invalid Username/Password");
                return ResponseEntity.status(UNAUTHORIZED).body("Invalid Username/Password");
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
            ApplicationUser appUser = (ApplicationUser) authentication.getPrincipal();
            String token = jwtUtils.generateTokenFromUsername(appUser);
            //ResponseCookie jwtCookie = jwtUtils.generateJwtCookie();

            List<String> roles = appUser.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            HashMap<String, Object> res = new HashMap<>();
            res.put("Token", token);
            res.put("Message","login successful");
            res.put("User",appUser);
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token)
                    .body(res);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.toString()));
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
        }else if(svUserDTO.getRoles().toUpperCase(Locale.ROOT).equals("ROLE_ADMIN")) {
            Role role =new Role();
            role.setName(ERole.ROLE_ADMIN);
            svUser.setRoles(role);
            return svUser;
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Role not valid");
        }

    }
}
