package com.orphan.common.repository;

import com.orphan.common.entity.Benefactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BenefactorRepository extends JpaRepository<Benefactor,Integer> {
    @Query("select b from Benefactor b where b.phone = ?1 ")
    Optional<Benefactor> findByPhone( String phone);

    @Query("select (count(b) > 0) from Benefactor b where b.phone = ?1")
    boolean existsByPhone(String phone);


}
