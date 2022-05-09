package com.orphan.common.repository;

import com.orphan.common.entity.User;
import com.orphan.common.response.ChildrenStatisticsByDateResponse;
import com.orphan.common.response.StatisticsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    /**
     * Get user by login id
     *
     * @param loginId request
     * @return User
     */
    Optional<User> findByLoginId(Integer loginId);

    boolean existsByEmail(String email);

    boolean existsByIdentification(String identification);

    boolean existsByLoginId(Integer loginId);

//    Page<User> findByOrderByFullNameAsc(Pageable pageable);

    @Query("select u from User u inner join u.roles roles where roles.name = ?1")
    List<User> findByRoles_Name(String name);

    @Query("select u from User u inner join u.roles roles where roles.name = ?1 order by u.createdAt")
    Page<User> findByRoles_NameOrderByCreatedAtAsc(String name, Pageable pageable);

    @Query("select u from User u inner join u.roles roles " +
            "where roles.name = ?1 and u.UserStatus = ?2 " +
            "order by u.recoveryExpirationDate")
    List<User> findByRoles_NameAndUserStatusOrderByRecoveryExpirationDateAsc1(String name, String UserStatus);

    @Query("select u from User u inner join u.roles roles " +
            "where roles.name = ?1 and u.UserStatus = ?2 " +
            "order by u.recoveryExpirationDate")
    Page<User> findByRoles_NameAndUserStatusOrderByRecoveryExpirationDateAsc(String name, String UserStatus, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update User u set u.UserStatus = ?1, u.recoveryExpirationDate = ?2 where u.loginId = ?3")
    void updateUserStatusAndRecoveryExpirationDateByLoginId(String UserStatus, Date recoveryExpirationDate, Integer loginId);

    @Transactional
    @Modifying
    @Query("delete from User u where u.recoveryExpirationDate=?1 and u.UserStatus = ?2")
    void deleteByRecoveryExpirationDateAndUserStatus(Date recoveryExpirationDate, String UserStatus);


    //view 
    @Query("select u from User u where u.UserStatus = ?1 order by u.recoveryExpirationDate")
    Page<User> findByUserDeleted(String UserStatus, Pageable pageable);


    @Query("select u from User u where u.UserStatus = ?1 order by u.recoveryExpirationDate")
    List<User> findByUserDeleted(String UserStatus);


    @Query("select u from User u inner join u.roles roles " +
            "where roles.name = ?1 and u.UserStatus = ?2 " +
            "order by u.createdAt")
    List<User> findByRoleAndStatus(String name, String UserStatus);

    @Query("select u from User u inner join u.roles roles " +
            "where roles.name = ?1 and u.UserStatus = ?2 " +
            "order by u.createdAt")
    Page<User> findByRoleAndStatus(String name, String UserStatus, Pageable pageable);


    //thong ke
    @Query("select u from User u where u.UserStatus = ?1 order by u.createdAt")
    List<User> findByUserActived(String UserStatus);

    @Query("select u from User u where u.UserStatus = ?1 order by u.createdAt")
    Page<User> findByUserActived(String UserStatus, Pageable pageable);



    @Query(value = "select roles.name as keyword,count(distinct u) as value from User u inner join u.roles roles group by roles.name")
    List<StatisticsResponse> countUserByRole();


    @Query(value = "select u.gender as keyword , count(distinct u) as value from User u group by u.gender ")
    List<StatisticsResponse> countUserByGender();


}
