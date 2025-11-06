package com.csbets.vcsbets.service;

import com.csbets.vcsbets.dto.match.MatchDto;
import com.csbets.vcsbets.dto.match.MatchResultDto;
import com.csbets.vcsbets.entity.match.Match;
import com.csbets.vcsbets.entity.match.MatchMap;
import com.csbets.vcsbets.entity.match.MatchResult;
import com.csbets.vcsbets.entity.match.Series;
import com.csbets.vcsbets.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class MatchService {

    private final MatchRepository matchRepository;
    private final SeriesService seriesService;

    public void fillMatchResults(Long matchId, MatchResultDto matchResultDto) {
        Match match = matchRepository.findById(matchId).orElse(null);
        assert match != null;
        match.setTeam1Rounds(matchResultDto.team1Rounds());
        match.setTeam2Rounds(matchResultDto.team2Rounds());
        match.setMatchResult(matchResultDto.team1Rounds() > matchResultDto.team2Rounds()
                ? MatchResult.TEAM1_WON : MatchResult.TEAM2_WON);
        Series series = match.getSeriesOn();
        seriesService.changeSeriesResult(series.getId(),
                matchResultDto.team1Rounds() > matchResultDto.team2Rounds());
        matchRepository.save(match);
    }

    public void setMapToMatch(MatchMap map, Long matchId) {
        Match match = matchRepository.findById(matchId).orElse(null);
        assert match != null;
        match.setMap(map);
        matchRepository.save(match);
    }

    public List<MatchDto> getAllMatchesBySeries(Long seriesId) {
        Series series = seriesService.getSeries(seriesId);
        return matchRepository.findAllBySeriesOn(series).stream().map(this::toDto).toList();
    }

    private MatchDto toDto(Match match) {
        return new MatchDto(
                match.getId(),
                match.getSeriesOn().getId(),
                match.getSeriesOrder(),
                match.getTeam1Rounds(),
                match.getTeam2Rounds(),
                match.getMatchResult(),
                match.getMap()
        );
    }
}
