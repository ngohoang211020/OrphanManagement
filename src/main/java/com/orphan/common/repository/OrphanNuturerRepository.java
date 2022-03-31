package com.orphan.common.repository;

import com.orphan.common.entity.OrphanNurturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrphanNuturerRepository extends JpaRepository<OrphanNurturer,Integer> {

    Optional<OrphanNurturer> findByFullName(String fullName);
}
