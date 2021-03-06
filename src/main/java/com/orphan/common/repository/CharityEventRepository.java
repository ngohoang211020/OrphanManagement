package com.orphan.common.repository;

import com.orphan.common.entity.CharityEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CharityEventRepository extends JpaRepository<CharityEvent, Integer> {

    @Query("select c from CharityEvent c")
    Page<CharityEvent> findByOrderByDateOfEventAsc(Pageable pageable);

    @Transactional
    @Modifying
    @Query("update CharityEvent c set c.isCompleted = true where c.dateEnd < current_timestamp and c.isCompleted = false")
    void updateIsCompletedTrue();


}
