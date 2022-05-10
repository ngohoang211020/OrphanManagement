package com.orphan.common.repository;

import com.orphan.common.entity.OrphanIntroducer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrphanIntroducerRepository extends JpaRepository<OrphanIntroducer, Integer> {
    Optional<OrphanIntroducer> findByFullName(String fullName);

    boolean existsByEmail(String email);

    boolean existsByIdentification(String identification);

    @Query("select o from OrphanIntroducer o order by o.createdAt")
    Page<OrphanIntroducer> findByOrderByCreatedAtAsc(Pageable pageable);



}
