package com.orphan.common.repository;

import com.orphan.common.entity.BenefactorCharity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenefactorCharityRepository extends JpaRepository<BenefactorCharity,Integer> {
}
