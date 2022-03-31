package com.orphan.common.repository;

import com.orphan.common.entity.OrphanIntroducer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrphanIntroducerRepository extends JpaRepository<OrphanIntroducer, Integer> {
    Optional<OrphanIntroducer> findByFullName(String fullName);


}
