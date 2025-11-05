package com.csbets.vcsbets.controller;

import com.csbets.vcsbets.dto.bet.MatchOutcomeBetDto;
import com.csbets.vcsbets.dto.bet.TotalRoundsBetDto;
import com.csbets.vcsbets.entity.bet.Bet;
import com.csbets.vcsbets.service.BetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bets")
@RequiredArgsConstructor
public class BetsController {

    private final BetsService betsService;

    @PostMapping("/match-outcome")
    public ResponseEntity<String> placeMatchOutcomeBet(
            @RequestBody MatchOutcomeBetDto betDto,
            @RequestParam String username
    ) {
        betsService.placeMatchOutcomeBet(betDto, username);
        return ResponseEntity.ok("Match outcome bet placed or updated successfully.");
    }

    @PostMapping("/total-rounds")
    public ResponseEntity<String> placeTotalRoundsBet(
            @RequestBody TotalRoundsBetDto betDto,
            @RequestParam String steam64
    ) {
        betsService.placeTotalRoundsBet(betDto, steam64);
        return ResponseEntity.ok("Total rounds bet placed or updated successfully.");
    }

    @PostMapping("/withdraw/{matchId}")
    public ResponseEntity<String> withdrawalPlacedBets(@PathVariable Long matchId) {
        betsService.withdrawalPlacedBets(matchId);
        return ResponseEntity.ok("Withdrawal processed for match " + matchId);
    }

    @GetMapping
    public ResponseEntity<List<Bet>> getAllBets() {
        return ResponseEntity.ok(betsService.getAllBets());
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<?> getBetById(@PathVariable Long matchId) {
        return ResponseEntity.ok(betsService.getAllBetsByMatchId(matchId));
    }

    @GetMapping("/{username}/bets")
    public ResponseEntity<?> getBets(@PathVariable String username) {
        return ResponseEntity.ok(betsService.getAllBetsByUser(username));
    }
}
