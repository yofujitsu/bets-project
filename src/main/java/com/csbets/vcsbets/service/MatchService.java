package com.csbets.vcsbets.service;

import com.csbets.vcsbets.dto.bet.BetDto;
import com.csbets.vcsbets.dto.match.MatchDto;
import com.csbets.vcsbets.dto.match.MatchInitDto;
import com.csbets.vcsbets.entity.bet.Bet;
import com.csbets.vcsbets.entity.match.Match;
import com.csbets.vcsbets.entity.match.MatchResult;
import com.csbets.vcsbets.entity.match.MatchStatus;
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
    private final BetsService betsService;

    public void fillMatchData(Long id, MatchInitDto matchInitDto) {
        Match match = matchRepository.findById(id).orElse(null);
        assert match != null;
        match.setTeam1Name(matchInitDto.team1Name());
        match.setTeam2Name(matchInitDto.team2Name());
        match.setTeam1Players(matchInitDto.team1Players());
        match.setTeam2Players(matchInitDto.team2Players());
        match.setMap(matchInitDto.map());
        log.info("Set match data: id {}, {}, {}, {}",
                id, matchInitDto.map(), match.getTeam1Name(), match.getTeam2Name());
        matchRepository.save(match);
    }

    public void changeMatchStatus(Long id, MatchStatus matchStatus) {
        Match match = matchRepository.findById(id).orElse(null);
        assert match != null;
        match.setStatus(matchStatus);
        log.info("Match {} status set to: {}", id, match.getStatus());
        matchRepository.save(match);
    }

    public void changeMatchResult(Long id, short team1Score, short team2Score) {
        Match match = matchRepository.findById(id).orElse(null);
        assert match != null;
        match.setTeam1Score(team1Score);
        match.setTeam2Score(team2Score);
        match.setMatchResult(team1Score > team2Score ? MatchResult.TEAM1_WON : MatchResult.TEAM2_WON);
        log.info("Match {} result set to: {}", id, match.getMatchResult());
        changeMatchStatus(id, MatchStatus.ENDED);
        betsService.withdrawalPlacedBets(id);
        matchRepository.save(match);
    }

    public Match getMatch(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Матч не найден"));
    }

    public MatchDto getMatchDto(Long matchId) {
        return convertToMatchDTO(matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Матч не найден")));
    }

    public List<MatchDto> getAllMatches() {
        return matchRepository.findAll().stream()
                .map(this::convertToMatchDTO)
                .toList();
    }

    private MatchDto convertToMatchDTO(Match match) {
        return new MatchDto(
                match.getId(),
                match.getTeam1Name(),
                match.getTeam2Name(),
                match.getTeam1Players(),
                match.getTeam2Players(),
                match.getTeam1Score(),
                match.getTeam2Score(),
                match.getStatus(),
                match.getMatchResult(),
                match.getMap()
        );
    }


}
