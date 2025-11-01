package com.csbets.vcsbets.config;

import com.csbets.vcsbets.entity.match.Match;
import com.csbets.vcsbets.entity.match.MatchStatus;
import com.csbets.vcsbets.repository.MatchRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MatchesInitializer {

    private MatchRepository matchRepository;

    @PostConstruct
    public void initMatches() {
        if(matchRepository.count() == 0) {
            for(long i = 0; i < 16; ++i) {
                Match match = new Match();
                match.setId(i++);
                match.setStatus(MatchStatus.PRE_MATCH);
                matchRepository.save(match);
            }
        }
    }
}
