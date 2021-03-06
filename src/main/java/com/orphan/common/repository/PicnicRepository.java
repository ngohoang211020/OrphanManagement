package com.orphan.common.repository;


import com.orphan.common.entity.Picnic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PicnicRepository extends JpaRepository<Picnic, Integer> {

    @Query("select p from Picnic p")
    Page<Picnic> findByOrderByDateOfPicnicAsc(Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Picnic p set p.isCompleted = true where p.dateEnd < current_timestamp and p.isCompleted = false")
    void updateIsCompletedTrue();
}
