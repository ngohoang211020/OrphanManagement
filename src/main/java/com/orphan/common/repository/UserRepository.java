package com.orphan.common.repository;

import com.orphan.common.entity.User;
import com.orphan.common.response.StatisticsByDateResponse;
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
            "order by u.recoveryExpirationDate ASC ")
    List<User> findByRoleAndStatusDELETED(String name, String UserStatus);

    @Query("select u from User u inner join u.roles roles " +
            "where roles.name = ?1 and u.UserStatus = ?2 " +
            "order by u.recoveryExpirationDate asc ")
    Page<User> findByRoleAndStatusDELETED(String name, String UserStatus, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update User u set u.UserStatus = ?1, u.recoveryExpirationDate = ?2 where u.loginId = ?3")
    void updateUserStatusAndRecoveryExpirationDateByLoginId(String UserStatus, Date recoveryExpirationDate, Integer loginId);

    @Transactional
    @Modifying
    @Query("delete from User u where u.recoveryExpirationDate >= ?1 and u.UserStatus = ?2")
    void deleteByRecoveryExpirationDateAndUserStatus(Date recoveryExpirationDate, String UserStatus);


    //view
    @Query("select u from User u where u.UserStatus = ?1 order by u.recoveryExpirationDate asc ")
    Page<User> findByUserDeleted(String UserStatus, Pageable pageable);


    @Query("select u from User u where u.UserStatus = ?1 order by u.recoveryExpirationDate asc ")
    List<User> findByUserDeleted(String UserStatus);


    @Query("select u from User u inner join u.roles roles " +
            "where roles.name = ?1 and u.UserStatus = ?2 " +
            "order by u.loginId asc ")
    List<User> findByRoleAndStatus(String name, String UserStatus);

    @Query("select u from User u inner join u.roles roles " +
            "where roles.name = ?1 and u.UserStatus = ?2 " +
            "order by u.loginId asc")
    Page<User> findByRoleAndStatus(String name, String UserStatus, Pageable pageable);


    //thong ke
    @Query("select u from User u where u.UserStatus = ?1 order by u.loginId asc ")
    List<User> findByUserActived(String UserStatus);

    @Query("select u from User u where u.UserStatus = ?1 order by u.loginId asc ")
    Page<User> findByUserActived(String UserStatus, Pageable pageable);

    @Query(value = "select roles.name as keyword,count(distinct u) as value from User u inner join u.roles roles where u.UserStatus=?1 group by roles.name")
    List<StatisticsResponse> countUserByRole(String status);


    @Query(value = "select u.gender as keyword,count(distinct u) as value from User u  where u.UserStatus=?1 group by u.gender")
    List<StatisticsResponse> countUserByGender(String status);

    @Query(value = "select month(u.createdAt) as month,year(u.createdAt) as year, count(distinct u) as amount from User u where u.UserStatus=?1 group by month(u.createdAt),year(u.createdAt) ")
    List<StatisticsByDateResponse> countUserOnBoardByMonth(String status);

    @Query(value = "select year(u.createdAt) as year, count(distinct u) as amount from User u where u.UserStatus=?1 group by year(u.createdAt) ")
    List<StatisticsByDateResponse> countUserOnBoardByYear(String status);

    @Query(value = "select u from User u inner join u.roles roles where lower(concat(u.fullName,' ',u.address,' ',u.phone,' ',u.email,' ',roles.name,' ',roles.description,' ',u.identification)) like  lower(concat('%', ?1,'%')) and u.UserStatus=?2")
    Page<User> searchUser(String keyword,String status,Pageable pageable);
    @Query(value = "select u from User u inner join u.roles roles where  lower(concat(u.fullName,' ',u.address,' ',u.phone,' ',u.email,' ',roles.name,' ',roles.description,' ',u.identification)) like lower(concat('%', ?1,'%')) and u.UserStatus=?2 and roles.name='ROLE_EMPLOYEE'")
    Page<User> searchEmployee(String keyword,String status,Pageable pageable);

    @Transactional
    @Modifying
    @Query("update User u set u.password = ?1 where u.loginId = ?2")
    void updatePasswordByLoginId(String password, Integer loginId);

    @Query(value = "select month(u.recoveryExpirationDate) as month,year(u.recoveryExpirationDate) as year, count(distinct u) as amount from User u where u.UserStatus='DELETED' group by month(u.recoveryExpirationDate),year(u.recoveryExpirationDate)")
    List<StatisticsByDateResponse> countUserArchivedByMonth();

    @Query(value = "select year(u.recoveryExpirationDate) as year, count(distinct u) as amount from User u where u.UserStatus='DELETED' group by month(u.recoveryExpirationDate),year(u.recoveryExpirationDate)")
    List<StatisticsByDateResponse> countUserArchivedByYear();


}
