package com.csbets.vcsbets.config;

import com.csbets.vcsbets.entity.match.Match;
import com.csbets.vcsbets.entity.match.MatchStatus;
import com.csbets.vcsbets.repository.MatchRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class MatchesInitializer {

    private final MatchRepository matchRepository;

    @PostConstruct
    public void initMatches() {
        List<Match> matches = new ArrayList<>();
        if(matchRepository.findAll().isEmpty()) {
            for(long i = 1; i <= 14; ++i) {
                Match match = new Match();
                match.setStatus(MatchStatus.PRE_MATCH);
                matches.add(match);
            }
        }
        matchRepository.saveAll(matches);
    }
}
