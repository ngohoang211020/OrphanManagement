package com.orphan.common.repository;

import com.orphan.common.entity.CharityEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharityEventRepository extends JpaRepository<CharityEvent,Integer> {
    Page<CharityEvent> findByOrderByNameEventAsc(Pageable pageable);




}
