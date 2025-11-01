package com.csbets.vcsbets.service;

import com.csbets.vcsbets.dto.match.MatchInitDto;
import com.csbets.vcsbets.entity.match.Match;
import com.csbets.vcsbets.entity.match.MatchStatus;
import com.csbets.vcsbets.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    public void fillMatchData(Long id, MatchInitDto matchInitDto) {
        Match match = matchRepository.findById(id).orElse(null);
        assert match != null;
        match.setMatchDateTime(matchInitDto.matchDateTime());
        match.setTeam1Name(matchInitDto.team1Name());
        match.setTeam2Name(matchInitDto.team2Name());
        match.setMap(matchInitDto.map());
        matchRepository.save(match);
    }

    public void changeMatchStatus(Long id, MatchStatus matchStatus) {
        Match match = matchRepository.findById(id).orElse(null);
        assert match != null;
        match.setStatus(matchStatus);
        matchRepository.save(match);
    }
}
