package com.csbets.vcsbets.repository;

import com.csbets.vcsbets.entity.bet.Bet;
import com.csbets.vcsbets.entity.bet.MatchOutcomeBet;
import com.csbets.vcsbets.entity.bet.TotalRoundsBet;
import com.csbets.vcsbets.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BetsRepository extends JpaRepository<Bet, Long> {
    List<Bet> findAllBySeries_Id(Long seriesId);
    List<Bet> findAllByUser(User User);
    @Query("SELECT b FROM MatchOutcomeBet b WHERE b.user = :user AND b.series.id = :seriesId")
    Optional<MatchOutcomeBet> findMatchOutcomeBetByUserAndSeries_Id(@Param("user") User user, @Param("seriesId") Long seriesId);

    @Query("SELECT b FROM TotalRoundsBet b WHERE b.user = :user AND b.series.id = :seriesId")
    Optional<TotalRoundsBet> findTotalRoundsBetByUserAndSeries_Id(@Param("user") User user, @Param("seriesId") Long seriesId);
}
