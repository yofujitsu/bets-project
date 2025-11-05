package com.csbets.vcsbets.entity.user;

import com.csbets.vcsbets.entity.bet.Bet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    private Long id;
    private String username;
    private String password;
    private String steamLink;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Bet> placedBets = new ArrayList<>();
    private short creditBalance = 1800;
    private short winningsBalance = 0;
    private int placedBetsCount = 0;
    private double betsWinRate = 0.0;
    private UserRole role = UserRole.USER;
}
