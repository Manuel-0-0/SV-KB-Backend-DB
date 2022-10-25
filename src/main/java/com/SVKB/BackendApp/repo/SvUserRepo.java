package com.SVKB.BackendApp.repo;

import com.SVKB.BackendApp.model.SvUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface SvUserRepo extends JpaRepository<SvUser,Long> {

    public SvUser findByUsername(String username);

    public Boolean existsByUsername(String username);

}
