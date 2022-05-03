package com.orphan.common.repository;

import com.orphan.common.entity.FurnitureCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FurnitureCategoryRepository extends JpaRepository<FurnitureCategory, Integer> {
    FurnitureCategory findByCategoryName(String categoryName);

    Page<FurnitureCategory> findByOrderByCategoryNameAsc(Pageable pageable);


}

