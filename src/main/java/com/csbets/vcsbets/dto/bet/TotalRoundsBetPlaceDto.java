package com.csbets.vcsbets.dto.bet;

import com.csbets.vcsbets.entity.bet.TotalRoundsBetType;

public record TotalRoundsBetPlaceDto(
        Long matchId,
        TotalRoundsBetType type,
        short roundsCount
) {
}
