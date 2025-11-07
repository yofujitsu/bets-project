package com.csbets.vcsbets.dto.bet;

import com.csbets.vcsbets.entity.bet.BetResult;
import com.csbets.vcsbets.entity.bet.BetType;
import com.csbets.vcsbets.entity.bet.MatchOutcomeResult;
import com.csbets.vcsbets.entity.bet.TotalRoundsBetType;

import java.time.Instant;

public record BetDto(
        Long id,
        Long seriesId,
        BetType betType,
        BetResult betResult,
        short betAmount,
        double coefficient,
        short winningsAmount,
        Instant timestamp,
        String username,
        String team1Name,
        String team2Name,
        String teamName,
        MatchOutcomeResult matchOutcomeResult,
        TotalRoundsBetType totalRoundsBetType,
        short roundsCount
) {
}
