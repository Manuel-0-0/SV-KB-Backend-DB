package com.SVKB.BackendApp.DTOs;

import com.SVKB.BackendApp.model.Role;
import lombok.Data;

import java.util.Set;

@Data
public class SvUserDTO {
    private String name;
    private String username;
    private String password;
    private String roles;
}
