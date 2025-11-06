package com.csbets.vcsbets.dto.bet;

import com.csbets.vcsbets.entity.bet.TotalRoundsBetType;
import jakarta.validation.constraints.Positive;

public record TotalRoundsBetPlaceDto(
        Long seriesId,
        TotalRoundsBetType type,
        @Positive short roundsCount
) {
}
