package com.orphan.common.repository;

import com.orphan.common.entity.MailTrackingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailTrackingRepository extends JpaRepository<MailTrackingEntity,Integer> {
    @Query("select m from MailTrackingEntity m where m.isCompleted = false and m.isSendImmediately = true")
    List<MailTrackingEntity> findByIsCompletedAndIsSendImmediately();

    @Query("select m from MailTrackingEntity m order by m.dateSend DESC")
    Page<MailTrackingEntity> findByOrderByDateSendDesc(Pageable pageable);


}
