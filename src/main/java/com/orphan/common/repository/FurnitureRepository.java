package com.orphan.common.repository;

import com.orphan.common.entity.Furniture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FurnitureRepository extends JpaRepository<Furniture,Integer> {

    @Query("select f from Furniture f")
    Page<Furniture> findByOrderByCreatedAtAsc(Pageable pageable);

//    @Transactional
//    @Modifying
//    @Query("update Furniture f set f.goodQuantity = ?1, f.brokenQuantity = ?2 where f.specifyFurnitureRequestList = ?3")
//    void updateGoodQuantityAndBrokenQuantityBySpecifyFurnitureRequestList(Integer goodQuantity, Integer brokenQuantity, SpecifyFurnitureRequest specifyFurnitureRequestList);

    @Query("select f from Furniture f where lower(concat(f.furnitureName,' ',f.status)) like lower(concat('%', ?1,'%')) ")
    Page<Furniture> searchFurniture(String keyword, Pageable pageable);


}
