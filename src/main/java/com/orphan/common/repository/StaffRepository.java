package com.orphan.common.repository;

import com.orphan.common.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    boolean existsByEmail(String email);

    boolean existsByIdentification(String identification);

    @Modifying
    @Query("UPDATE Staff u set u.image= ?1, u.profPic= ?2 WHERE u.staffId= ?3")
    void updateStaffImage(String image,byte[] procPic, Integer staffId);
}
