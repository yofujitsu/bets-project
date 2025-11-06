package com.csbets.vcsbets.entity.match;

import jakarta.persistence.*;
import lombok.*;

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
    @ManyToOne
    @JoinColumn(name = "series_id")
    private Series seriesOn;
    private short seriesOrder;
    private short team1Rounds;
    private short team2Rounds;
    @Enumerated(EnumType.STRING)
    private MatchResult matchResult;
    @Enumerated(EnumType.STRING)
    private MatchMap map;
}
