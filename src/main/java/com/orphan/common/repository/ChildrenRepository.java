package com.orphan.common.repository;

import com.orphan.common.entity.Children;
import com.orphan.common.entity.OrphanIntroducer;
import com.orphan.common.entity.OrphanNurturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ChildrenRepository extends JpaRepository<Children, Integer> {
    Page<Children> findByOrderByFullNameAsc(Pageable pageable);

    @Query("select c from Children c where c.orphanIntroducer.introducerId = ?1")
    List<Children> findByOrphanIntroducer_IntroducerId(Integer introducerId);

    @Transactional
    @Modifying
    @Query("update Children c set c.orphanIntroducer = ?1 where c.orphanIntroducer.introducerId = ?2")
    void updateOrphanIntroducerByOrphanIntroducer_IntroducerId(OrphanIntroducer orphanIntroducer, Integer introducerId);

    @Transactional
    @Modifying
    @Query("update Children c set c.status = ?1, c.orphanNurturer = ?2 where c.orphanNurturer.nurturerId = ?3")
    void updateStatusAndOrphanNurturerByOrphanNurturer_NurturerId(String status, OrphanNurturer orphanNurturer, Integer nurturerId);



}
