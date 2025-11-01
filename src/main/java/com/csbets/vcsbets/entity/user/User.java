package com.csbets.vcsbets.entity.user;

import com.csbets.vcsbets.entity.bet.Bet;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "users")
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @UuidGenerator
    private UUID id;
    private String username;
    private String password;
    private String steamId64;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bet> placedBets = new ArrayList<>();
    private short creditBalance;
    private short winningsBalance;
    private int placedBetsCount;
    private double betsWinRate;
    private UserRole role;
}
