package com.orphan.common.repository;

import com.orphan.common.entity.OrphanIntroducer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrphanIntroducerRepository extends JpaRepository<OrphanIntroducer, Integer> {
}
