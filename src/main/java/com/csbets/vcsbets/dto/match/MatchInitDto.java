package com.csbets.vcsbets.dto.match;

import com.csbets.vcsbets.entity.match.MatchMap;

import java.time.Instant;
import java.util.List;

public record MatchInitDto(
        String team1Name,
        String team2Name,
        List<String> team1Players,
        List<String> team2Players,
        MatchMap map
) {
}
