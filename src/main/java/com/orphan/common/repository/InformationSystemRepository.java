package com.orphan.common.repository;

import com.orphan.common.entity.InformationSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformationSystemRepository extends JpaRepository<InformationSystem,Integer> {
}
