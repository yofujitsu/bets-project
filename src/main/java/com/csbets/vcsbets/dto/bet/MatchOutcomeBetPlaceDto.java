package com.csbets.vcsbets.dto.bet;

import com.csbets.vcsbets.entity.bet.MatchOutcomeResult;

public record MatchOutcomeBetPlaceDto(
        Long seriesId,
        MatchOutcomeResult matchOutcomeResult,
        String teamName
) {
}
