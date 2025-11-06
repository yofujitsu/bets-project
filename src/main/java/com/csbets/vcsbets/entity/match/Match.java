package com.csbets.vcsbets.entity.match;

import com.csbets.vcsbets.entity.bet.Bet;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "matches")
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "match", fetch = FetchType.LAZY)
    private List<Bet> placedBets = new ArrayList<>();
    private String team1Name = "team1";
    private String team2Name = "team2";
    @ElementCollection
    private List<String> team1Players = new ArrayList<>();
    @ElementCollection
    private List<String> team2Players = new ArrayList<>();
    private short team1Score = 0;
    private short team2Score = 0;
    private MatchStatus status = MatchStatus.PRE_MATCH;
    private MatchResult matchResult;
    private MatchMap map;
}
