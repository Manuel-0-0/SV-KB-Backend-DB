package com.SVKB.BackendApp.repo;

import com.SVKB.BackendApp.model.SvUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SvUserRepo extends JpaRepository<SvUser,Long> {

    public SvUser findByUsername(String username);
}
