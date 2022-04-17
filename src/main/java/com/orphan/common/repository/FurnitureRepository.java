package com.orphan.common.repository;

import com.orphan.common.entity.Furniture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FurnitureRepository extends JpaRepository<Furniture,Integer> {

    Page<Furniture> findByOrderByFurnitureNameAsc(Pageable pageable);

}
