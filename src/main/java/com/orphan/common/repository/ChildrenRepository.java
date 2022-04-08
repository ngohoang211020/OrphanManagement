package com.orphan.common.repository;

import com.orphan.common.entity.Children;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildrenRepository extends JpaRepository<Children, Integer> {
    Page<Children> findByOrderByFullNameAsc(Pageable pageable);

    @Modifying
    @Query("UPDATE Children u set u.image= ?1, u.profPic= ?2 WHERE u.id= ?3")
    void updateChildrenImage(String image,byte[] procPic, Integer childrenId);
}
