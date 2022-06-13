package com.orphan.common.repository;

import com.orphan.common.entity.InformationSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InformationSystemRepository extends JpaRepository<InformationSystem, Integer> {

    @Transactional
    @Modifying
    @Query("update InformationSystem i set i.fund = ?1")
    void updateFundBy(Long fund);

}
