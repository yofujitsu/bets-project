package com.csbets.vcsbets.entity.bet;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Entity
@DiscriminatorValue("TOTAL_ROUNDS")
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TotalRoundsBet extends Bet {
    @Enumerated(EnumType.STRING)
    private BetType matchOutcome = BetType.TOTAL_ROUNDS;
    private TotalRoundsBetType type;
    private short roundsCount;
}
