package com.csbets.vcsbets.entity.match;

import com.csbets.vcsbets.entity.bet.Bet;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "matches")
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "match", fetch = FetchType.LAZY)
    private List<Bet> placedBets = new ArrayList<>();
    private String team1Name;
    private String team2Name;
    @ElementCollection
    private List<String> team1Players = new ArrayList<>();
    @ElementCollection
    private List<String> team2Players = new ArrayList<>();
    private short team1Score;
    private short team2Score;
    private MatchStatus status;
    private MatchResult matchResult;
    private MatchMap map;
}
