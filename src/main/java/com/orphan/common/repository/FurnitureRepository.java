package com.orphan.common.repository;

import com.orphan.common.entity.Furniture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FurnitureRepository extends JpaRepository<Furniture,Integer> {
}
