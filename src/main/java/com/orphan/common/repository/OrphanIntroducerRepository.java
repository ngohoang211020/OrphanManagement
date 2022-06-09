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

    @Query("select o from OrphanIntroducer o")
    Page<OrphanIntroducer> findByOrderByCreatedAtAsc(Pageable pageable);


    @Query("select o from OrphanIntroducer o where lower(concat(o.email,' ',o.phone,' ',o.address,' ',o.fullName,' ',o.identification)) like lower(concat('%', ?1,'%'))")
    Page<OrphanIntroducer> searchIntroducer(String keyword,Pageable pageable);

}
