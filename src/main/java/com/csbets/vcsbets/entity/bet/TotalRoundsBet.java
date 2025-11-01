package com.csbets.vcsbets.entity.bet;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TOTAL_ROUNDS")
public class TotalRoundsBet extends Bet {
    private TotalRoundsBetType type;
    private short roundsCount;
}
