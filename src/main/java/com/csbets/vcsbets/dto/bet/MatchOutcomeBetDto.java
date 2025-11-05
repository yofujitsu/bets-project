package com.csbets.vcsbets.dto.bet;

import com.csbets.vcsbets.entity.bet.MatchOutcomeResult;

public record MatchOutcomeBetDto(
        Long matchId,
        MatchOutcomeResult matchOutcomeResult,
        String teamName
) {
}
