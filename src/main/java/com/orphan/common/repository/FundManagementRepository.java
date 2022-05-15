package com.orphan.common.repository;

import com.orphan.common.entity.FundManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface FundManagementRepository extends JpaRepository<FundManagement,Integer> {
    @Query("select f from FundManagement f where f.date = ?1")
    FundManagement findByDate(Date date);

}
