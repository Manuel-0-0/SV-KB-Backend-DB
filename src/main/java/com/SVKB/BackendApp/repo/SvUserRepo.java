package com.SVKB.BackendApp.repo;

import com.SVKB.BackendApp.model.SvUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SvUserRepo extends JpaRepository<SvUser,Long> {

    public SvUser findByUsername(String username);

    public Boolean existsByUsername(String username);


    @Transactional
    @Modifying
    @Query("delete from SvUser s where s.username = ?1")
    void deleteByUsername(String username);

}
