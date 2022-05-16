package com.orphan.common.repository;

import com.orphan.common.entity.SpecifyFurnitureRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SpecifyFurnitureRequestRepository extends JpaRepository<SpecifyFurnitureRequest,Integer> {
//    @Query("select s from SpecifyFurnitureRequest s " +
//            "where s.furnitureRequestForm.status = ?1 and s.furnitureRequestForm.finishDate = ?2")
//    List<SpecifyFurnitureRequest> findByFurnitureRequestForm_StatusAndFurnitureRequestForm_FinishDate(String status, Date finishDate);
//
//    @Query("select s from SpecifyFurnitureRequest s " +
//            "where s.status = ?1 and s.furnitureRequestForm.status = ?2 and s.furnitureRequestForm.finishDate = ?3")
//    List<SpecifyFurnitureRequest> findByStatusAndFurnitureRequestForm_StatusAndFurnitureRequestForm_FinishDate(Boolean status, String status1, Date finishDate);

//    @Query("select s from SpecifyFurnitureRequest s where s.status = ?1")
//    List<SpecifyFurnitureRequest> findByStatus(Boolean status);


}
