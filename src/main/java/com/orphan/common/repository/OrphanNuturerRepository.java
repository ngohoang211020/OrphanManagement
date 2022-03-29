package com.orphan.common.repository;

import com.orphan.common.entity.OrphanNuturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrphanNuturerRepository extends JpaRepository<OrphanNuturer,Integer> {
}
