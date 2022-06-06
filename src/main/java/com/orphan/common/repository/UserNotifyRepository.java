package com.orphan.common.repository;

import com.orphan.common.entity.UserNotifyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotifyRepository extends JpaRepository<UserNotifyEntity, Integer> {
    @Query("select u from UserNotifyEntity u where u.user.loginId = ?1 order by u.dateSend DESC")
    List<UserNotifyEntity> findByUser_LoginIdOrderByDateSendDesc(Integer loginId);

}
