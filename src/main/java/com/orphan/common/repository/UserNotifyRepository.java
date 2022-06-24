package com.orphan.common.repository;

import com.orphan.common.entity.UserNotifyEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserNotifyRepository extends JpaRepository<UserNotifyEntity, Integer> {

    @Query("select u from UserNotifyEntity u where u.user.loginId = ?1 and u.type = 'MAIL_NOTIFY' order by u.dateSend DESC")
    List<UserNotifyEntity> findByUser_LoginIdOrderByDateSendDesc(Integer loginId);

    @Transactional
    @Modifying
    @Query("delete from UserNotifyEntity u where u.user.loginId = ?1")
    void deleteByUserId(Integer loginId);

}
