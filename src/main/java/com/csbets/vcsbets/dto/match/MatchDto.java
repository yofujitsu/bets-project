package com.csbets.vcsbets.dto.match;

import com.csbets.vcsbets.entity.match.MatchMap;
import com.csbets.vcsbets.entity.match.MatchResult;

public record MatchDto(
        Long id,
        Long seriesId,
        short seriesOrder,
        short team1Rounds,
        short team2Rounds,
        MatchResult matchResult,
        MatchMap map
) {
}
