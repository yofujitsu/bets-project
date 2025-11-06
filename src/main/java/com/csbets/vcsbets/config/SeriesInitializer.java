package com.csbets.vcsbets.config;

import com.csbets.vcsbets.entity.match.Match;
import com.csbets.vcsbets.entity.match.Series;
import com.csbets.vcsbets.entity.match.SeriesFormat;
import com.csbets.vcsbets.repository.MatchRepository;
import com.csbets.vcsbets.repository.SeriesRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SeriesInitializer {

    private final SeriesRepository seriesRepository;
    private final MatchRepository matchRepository;
    private static final List<String> MATCH_PAIRS = List.of(
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
    @Transactional
    public void initMatches() {
        List<Series> seriesList = new ArrayList<>();
        if (seriesRepository.findAll().isEmpty()) {
            for (long i = 1; i <= 14; ++i) {
                Series series = new Series();
                seriesRepository.save(series);
                if(i > 4) {
                    series.setSeriesFormat(SeriesFormat.BO3);
                    Match match = new Match();
                    Match match2 = new Match();
                    Match match3 = new Match();
                    match.setSeriesOrder((short) 1);
                    match2.setSeriesOrder((short) 2);
                    match3.setSeriesOrder((short) 3);
                    match.setSeriesOn(series);
                    match2.setSeriesOn(series);
                    match3.setSeriesOn(series);
                    List<Match> matches = List.of(match, match2, match3);
                    series.setMatches(matches);
                    matchRepository.saveAll(matches);
                }
                seriesList.add(series);
            }
        }
        int counter = 0;
        for (int i = 0; i < 4; ++i) {
            Series series = seriesList.get(i);
            series.setTeam1Players(List.of(MATCH_PAIRS.get(counter),
                    MATCH_PAIRS.get(counter + 1)));
            series.setTeam2Players(List.of(MATCH_PAIRS.get(counter + 2),
                    MATCH_PAIRS.get(counter + 3)));
            series.setSeriesFormat(SeriesFormat.BO1);
            Match match = new Match();
            match.setSeriesOn(series);
            match.setSeriesOrder((short) 1);
            matchRepository.save(match);
            series.setMatches(List.of(match));
            counter += 4;
        }
        seriesRepository.saveAll(seriesList);
    }
}
