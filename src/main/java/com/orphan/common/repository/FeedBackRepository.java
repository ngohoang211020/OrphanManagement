package com.orphan.common.repository;

import com.orphan.common.entity.FeedbackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBackRepository extends JpaRepository<FeedbackEntity,Integer> {
    @Query("select f from FeedbackEntity f")
    Page<FeedbackEntity> findByOrderByCreatedAtDesc(Pageable pageable);

}
