package com.csbets.vcsbets.config;

import com.csbets.vcsbets.entity.match.Match;
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
    private final List<String> initMatchPairs = List.of(
            "Scuderia", "3Be3Da yIIaJla上帝",
            "frixieq", "nikno8",
            "rel1segod", "expiasian",
            "yayebok228", "Sa1der",
            "ryousuke", "Sh1ny",
            "whynot", "YouMore",
            "WhatIsLove", "Vinni Boombatz",
            "Deserma", "LexxTr1ck"
    );

    @PostConstruct
    public void initMatches() {
        List<Match> matches = new ArrayList<>();
        if (matchRepository.findAll().isEmpty()) {
            for (long i = 1; i <= 14; ++i) {
                Match match = new Match();
                matches.add(match);
            }
        }
        int counter = 0;
        for (int i = 0; i < 4; ++i) {
            Match match = matches.get(i);
            match.setTeam1Players(List.of(initMatchPairs.get(counter),
                    initMatchPairs.get(counter + 1)));
            match.setTeam2Players(List.of(initMatchPairs.get(counter + 2),
                    initMatchPairs.get(counter + 3)));
            counter += 4;
        }
        matchRepository.saveAll(matches);
    }
}
