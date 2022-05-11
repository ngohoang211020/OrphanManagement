package com.orphan.common.repository;

import com.orphan.common.entity.OrphanIntroducer;
import com.orphan.common.entity.OrphanNurturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrphanNurturerRepository extends JpaRepository<OrphanNurturer, Integer> {

    Optional<OrphanNurturer> findByFullName(String fullName);

    boolean existsByEmail(String email);

    boolean existsByIdentification(String identification);

    @Query("select o from OrphanNurturer o order by o.createdAt")
    Page<OrphanNurturer> findByOrderByCreatedAtAsc(Pageable pageable);

    @Query("select o from OrphanNurturer o where concat(o.email,' ',o.phone,' ',o.address,' ',o.fullName,' ',o.identification) like %?1%")
    Page<OrphanNurturer> searchNurturer(String keyword, Pageable pageable);
}
