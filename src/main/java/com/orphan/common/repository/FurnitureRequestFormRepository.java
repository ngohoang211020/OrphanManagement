package com.orphan.common.repository;

import com.orphan.common.entity.FurnitureRequestForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface FurnitureRequestFormRepository extends JpaRepository<FurnitureRequestForm, Integer> {
    @Transactional
    @Modifying
    @Query("update FurnitureRequestForm f set f.status = ?1 where f.status = ?2 and f.finishDate = ?3")
    void updateStatusByStatusAndFinishDate(String status, String status1, Date finishDate);

    @Query("select f from FurnitureRequestForm f order by f.createdAt")
    Page<FurnitureRequestForm> findByOrderByCreatedAtAsc(Pageable pageable);

    @Query("select f from FurnitureRequestForm f where f.finishDate = ?1 and f.status = ?2")
    List<FurnitureRequestForm> findByFinishDateAndStatus(Date finishDate, String status);


}
