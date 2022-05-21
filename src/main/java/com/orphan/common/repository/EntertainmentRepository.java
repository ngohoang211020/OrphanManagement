package com.orphan.common.repository;


import com.orphan.common.entity.Entertainment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntertainmentRepository extends JpaRepository<Entertainment,Integer> {

    Page<Entertainment> findByOrderByEntertainmentAsc(Pageable pageable);


}
