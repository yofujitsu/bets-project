package com.csbets.vcsbets.repository;

import com.csbets.vcsbets.entity.bet.Bet;
import com.csbets.vcsbets.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BetsRepository extends JpaRepository<Bet, UUID> {
    List<Bet> findAllBySeries_Id(Long seriesId);

    Optional<Bet> findTotalRoundsBetByUserAndSeries_Id(User User, Long seriesId);

    Optional<Bet> findMatchOutcomeBetByUserAndSeries_Id(User User, Long seriesId);

    List<Bet> findAllByUser(User User);
}
