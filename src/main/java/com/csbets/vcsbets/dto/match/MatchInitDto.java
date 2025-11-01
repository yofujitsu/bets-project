package com.csbets.vcsbets.dto.match;

import com.csbets.vcsbets.entity.match.MatchMap;

import java.time.Instant;

public record MatchInitDto(
        String team1Name,
        String team2Name,
        MatchMap map,
        Instant matchDateTime
) {
}
