package com.orphan.common.repository;

import com.orphan.common.entity.Children;
import com.orphan.common.entity.OrphanIntroducer;
import com.orphan.common.entity.OrphanNurturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface ChildrenRepository extends JpaRepository<Children, Integer> {

    @Query("select c from Children c order by c.createdAt")
    Page<Children> findByOrderByCreatedAtAsc(Pageable pageable);

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

    @Query(value = "select count(distinct c) from Children c  where Month(c.introductoryDate) =?1 and Year(c.introductoryDate)=?2")
    Integer countChildrenIntroduceByMonth(Integer month,Integer year);

    @Query(value = "select count(distinct c) from Children c  where Year(c.introductoryDate)=?1")
    Integer countChildrenIntroduceByYear(Integer year);

    @Query(value = "select count(distinct c) from Children c  where Month(c.adoptiveDate) =?1 and Year(c.adoptiveDate)=?2")
    Integer countChildrenAdoptiveByMonth(Integer month,Integer year);

    @Query(value = "select count(distinct c) from Children c  where Year(c.adoptiveDate)=?1")
    Integer countChildrenAdoptiveByYear(Integer year);

    @Query(value = "select count(distinct c) from Children c  where c.gender=?1")
    Integer countChildrenByGender(Boolean gender);
}
