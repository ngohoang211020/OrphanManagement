package com.orphan.common.repository;

import com.orphan.common.entity.FundManagement;
import com.orphan.common.response.StatisticsByDateResponse;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FundManagementRepository extends JpaRepository<FundManagement, Integer> {

    @Query("select f from FundManagement f where f.date = ?1")
    FundManagement findByDate(Date date);

    @Query("select f from FundManagement f where f.date >= ?1 and f.type = ?2")
    Page<FundManagement> findByDateAndTypeOrderByDateDesc(LocalDateTime date, String type,
            Pageable pageable);

    @Query("select f from FundManagement f where f.type = ?1 and f.date = ?2 and f.description like ?3")
    Optional<FundManagement> findByTypeAndDateAndDescriptionIsLike(String type, Date date,
            String description);

    @Query("select f from FundManagement f where f.type = ?1 and f.description like ?2")
    Optional<FundManagement> findByTypeAndDateAndDescriptionIsLike(String type,
            String description);

    @Query(value = "select month(f.date) as month,year(f.date) as year, sum(f.money) as amount from FundManagement f where f.type =?1 group by month(f.date),year(f.date) order by month(f.date) desc ,year(f.date) desc ")
    List<StatisticsByDateResponse> moneyByMonth(String type);

    @Query(value = "select year(f.date) as year, sum(f.money) as amount from FundManagement f where f.type =?1 group by year(f.date)")
    List<StatisticsByDateResponse> moneyByYear(String type);

}
