package com.orphan.common.repository;

import com.orphan.common.entity.ReceivedChildrenInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceivedChildrenInfoRepository extends JpaRepository<ReceivedChildrenInfo,Integer> {
}
