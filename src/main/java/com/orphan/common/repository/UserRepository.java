package com.orphan.common.repository;

import com.orphan.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    @Modifying
    @Query("UPDATE User u set u.image= ?1, u.profPic= ?2 WHERE u.loginId= ?3")
    void updateUserImage(String image,byte[] procPic, Integer userId);

}
