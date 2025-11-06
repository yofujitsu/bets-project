package com.csbets.vcsbets.dto.match;

import com.csbets.vcsbets.entity.match.MatchMap;
import com.csbets.vcsbets.entity.match.MatchResult;
import com.csbets.vcsbets.entity.match.MatchStatus;

import java.util.List;

public record MatchDto(
        Long id,
        String team1Name,
        String team2Name,
        List<String> team1Players,
        List<String> team2Players,
        short team1Score,
        short team2Score,
        MatchStatus status,
        MatchResult matchResult,
        MatchMap map
) {
}
