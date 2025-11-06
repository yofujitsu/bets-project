package com.csbets.vcsbets.dto.match;

import java.util.List;

public record SeriesInitDto(
        List<String> team1Players,
        List<String> team2Players
) {
}
