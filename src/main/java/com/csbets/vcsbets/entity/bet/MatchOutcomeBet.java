package com.csbets.vcsbets.entity.bet;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Entity
@DiscriminatorValue("MATCH_WINNER")
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchOutcomeBet extends Bet {
    @Enumerated(EnumType.STRING)
    private BetType matchOutcome = BetType.MATCH_WINNER;
    private String teamName;
    private MatchOutcomeResult matchOutcomeResult;
}
