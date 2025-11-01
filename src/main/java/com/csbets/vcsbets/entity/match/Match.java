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
    private Instant matchDateTime;
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bet> placedBets = new ArrayList<>();
    private String team1Name;
    private String team2Name;
    private short team1Score;
    private short team2Score;
    private MatchStatus status;
    private MatchResult matchResult;
    private MatchMap map;
}
