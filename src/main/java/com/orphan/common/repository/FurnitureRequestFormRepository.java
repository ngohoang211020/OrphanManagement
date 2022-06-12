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

@Repository
public interface FurnitureRequestFormRepository extends JpaRepository<FurnitureRequestForm, Integer> {
    @Transactional
    @Modifying
    @Query("update FurnitureRequestForm f set f.status = ?1 where f.status = ?2 and f.finishDate = ?3")
    void updateStatusByStatusAndFinishDate(String status, String status1, Date finishDate);

    @Query("select f from FurnitureRequestForm f")
    Page<FurnitureRequestForm> findByOrderByCreatedAtAsc(Pageable pageable);

//    @Query("select f from FurnitureRequestForm f where f.finishDate = ?1 and f.status = ?2")
//    List<FurnitureRequestForm> findByFinishDateAndStatus(Date finishDate, String status);
//
//    List<FurnitureRequestForm> findByStartDateAndStatus(Date startDate, String status);

    @Query("select f from FurnitureRequestForm f where f.user.loginId = ?1")
    Page<FurnitureRequestForm> findByUser_LoginIdOrderByStartDateAsc(Integer loginId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update FurnitureRequestForm f set f.status = ?1 where f.furnitureRequestId=?2")
    void updateStatusByStatus(String status,Integer furnitureRequestId);

    @Query("select (count(f) > 0) from FurnitureRequestForm f where f.furnitureRequestId = ?1")
    boolean existsByFurnitureRequestId(Integer furnitureRequestId);

    @Transactional
    @Modifying
    @Query("update FurnitureRequestForm f set f.status = ?1, f.finishDate = ?2, f.totalPrice = ?3 " +
            "where f.furnitureRequestId = ?4")
    void confirmFinish(String status, Date finishDate, Long totalPrice, Integer furnitureRequestId);

    @Transactional
    @Modifying
    @Query("update FurnitureRequestForm f set f.deadlineDate = ?1 where f.status = ?2 and f.furnitureRequestId = ?3")
    void extensionOfTime(Date deadlineDate, String status, Integer furnitureRequestId);


}
