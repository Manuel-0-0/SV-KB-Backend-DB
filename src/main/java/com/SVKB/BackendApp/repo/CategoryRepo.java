package com.SVKB.BackendApp.repo;

import com.SVKB.BackendApp.model.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository< CategoryModel,Long> {
}
