package com.csbets.vcsbets.dto.bet;

import com.csbets.vcsbets.entity.bet.TotalRoundsBetType;

public record TotalRoundsBetDto(
        Long matchId,
        TotalRoundsBetType type,
        short roundsCount
) {
}
