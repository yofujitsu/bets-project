package com.csbets.vcsbets.dto.match;

import com.csbets.vcsbets.entity.match.SeriesFormat;
import com.csbets.vcsbets.entity.match.SeriesResult;
import com.csbets.vcsbets.entity.match.SeriesStatus;

import java.util.List;

public record SeriesDto(
        Long id,
        String team1Name,
        String team2Name,
        List<String> team1Players,
        List<String> team2Players,
        short team1Score,
        short team2Score,
        SeriesStatus status,
        SeriesResult seriesResult,
        SeriesFormat seriesFormat
) {
}
