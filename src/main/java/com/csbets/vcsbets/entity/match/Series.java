package com.csbets.vcsbets.entity.match;

import com.csbets.vcsbets.entity.bet.Bet;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "series")
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "series", fetch = FetchType.LAZY)
    private List<Bet> placedBets = new ArrayList<>();
    @OneToMany(mappedBy = "seriesOn", fetch = FetchType.LAZY)
    private List<Match> matches = new ArrayList<>();
    private String team1Name = "team1";
    private String team2Name = "team2";
    @ElementCollection
    private List<String> team1Players = new ArrayList<>();
    @ElementCollection
    private List<String> team2Players = new ArrayList<>();
    private short team1Score = 0;
    private short team2Score = 0;
    @Enumerated(EnumType.STRING)
    private SeriesStatus status = SeriesStatus.PRE_MATCH;
    @Enumerated(EnumType.STRING)
    private SeriesResult seriesResult;
    @Enumerated(EnumType.STRING)
    private SeriesFormat seriesFormat;
}
