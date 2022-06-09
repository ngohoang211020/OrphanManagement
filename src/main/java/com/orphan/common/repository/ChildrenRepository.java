package com.orphan.common.repository;

import com.orphan.common.entity.Children;
import com.orphan.common.entity.OrphanIntroducer;
import com.orphan.common.entity.OrphanNurturer;
import com.orphan.common.response.StatisticsByDateResponse;
import com.orphan.common.response.StatisticsResponse;
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

    @Query("select c from Children c")
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

    @Query(value = "select month(c.introductoryDate) as month,year(c.introductoryDate) as year, count(distinct c) as amount from Children c group by month(c.introductoryDate),year(c.introductoryDate) ")
    List<StatisticsByDateResponse> countChildrenByIntroduceDate();

    @Query(value = "select year(c.introductoryDate) as year, count(distinct c) as amount from Children c group by year(c.introductoryDate) ")
    List<StatisticsByDateResponse> countChildrenByIntroduceDateYear();
    @Query(value = "select month(c.adoptiveDate) as month,year(c.adoptiveDate) as year, count(distinct c) as amount from Children c group by month(c.adoptiveDate),year(c.adoptiveDate) ")
    List<StatisticsByDateResponse> countChildrenByAdoptiveDate();

    @Query(value = "select year(c.adoptiveDate) as year, count(distinct c) as amount from Children c where c.status='RECEIVED' group by year(c.adoptiveDate)")
    List<StatisticsByDateResponse> countChildrenByAdoptiveDateYear();

    @Query(value = "select c.gender as keyword , count(distinct c) as value from Children c group by c.gender ")
    List<StatisticsResponse> countChildrenByGender();

   @Query(value = "select c from Children c where lower(c.fullName) like lower(concat('%', ?1,'%')) ")
    Page<Children> searchChildren(String keyword,Pageable pageable);

}

