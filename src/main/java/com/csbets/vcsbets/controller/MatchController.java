package com.csbets.vcsbets.controller;

import com.csbets.vcsbets.dto.match.MatchDto;
import com.csbets.vcsbets.dto.match.MatchResultDto;
import com.csbets.vcsbets.entity.match.MatchMap;
import com.csbets.vcsbets.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/{seriesId}")
    public List<MatchDto> getAllMatchesBySeries(@PathVariable Long seriesId) {
        return matchService.getAllMatchesBySeries(seriesId);
    }

    @PostMapping("/{matchId}/map")
    public void setMapToMatch(@PathVariable Long matchId, @RequestParam MatchMap matchMap) {
        matchService.setMapToMatch(matchMap, matchId);
    }

    @PostMapping("/{matchId}")
    public void fillMatchResults(@PathVariable Long matchId, @RequestBody MatchResultDto matchDto) {
        matchService.fillMatchResults(matchId, matchDto);
    }

}
