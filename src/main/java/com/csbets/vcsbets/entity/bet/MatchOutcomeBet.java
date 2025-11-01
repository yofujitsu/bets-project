package com.csbets.vcsbets.entity.bet;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MATCH_WINNER")
public class MatchOutcomeBet extends Bet {
    private String teamName;
    private MatchOutcomeResult matchOutcomeResult;
}
