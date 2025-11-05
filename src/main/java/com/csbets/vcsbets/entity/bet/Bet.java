package com.csbets.vcsbets.entity.bet;

import com.csbets.vcsbets.entity.match.Match;
import com.csbets.vcsbets.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity(name = "bets")
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "bet_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;
    @Enumerated(EnumType.STRING)
    private BetResult betResult;
    private short betAmount;
    private double coefficient;
    private short winningsAmount;
    @CreationTimestamp
    private Instant timestamp;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
}
