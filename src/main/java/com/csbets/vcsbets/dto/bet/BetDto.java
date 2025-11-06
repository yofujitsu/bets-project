package com.csbets.vcsbets.dto.bet;

import com.csbets.vcsbets.entity.bet.BetResult;
import com.csbets.vcsbets.entity.bet.BetType;
import com.csbets.vcsbets.entity.bet.MatchOutcomeResult;
import com.csbets.vcsbets.entity.bet.TotalRoundsBetType;

import java.time.Instant;

public record BetDto(
        Long id,
        String steamId64,
        Long matchId,
        BetType betType,
        BetResult betResult,
        short betAmount,
        double coefficient,
        short winningsAmount,
        Instant timestamp,
        String username,
        String teamName,
        MatchOutcomeResult matchOutcomeResult,
        TotalRoundsBetType totalRoundsBetType,
        short roundsCount
) {
}
