package com.csbets.vcsbets.controller;

import com.csbets.vcsbets.dto.bet.MatchOutcomeBetPlaceDto;
import com.csbets.vcsbets.dto.bet.TotalRoundsBetPlaceDto;
import com.csbets.vcsbets.service.BetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bets")
@RequiredArgsConstructor
public class BetsController {

    private final BetsService betsService;

    @PostMapping("/series-outcome")
    public ResponseEntity<String> placeMatchOutcomeBet(
            @RequestBody MatchOutcomeBetPlaceDto betDto,
            @RequestParam String username
    ) {
        betsService.placeMatchOutcomeBet(betDto, username);
        return ResponseEntity.ok("Match outcome bet placed or updated successfully.");
    }

    @PostMapping("/total")
    public ResponseEntity<String> placeTotalRoundsBet(
            @RequestBody TotalRoundsBetPlaceDto betDto,
            @RequestParam String username
    ) {
        betsService.placeTotalRoundsBet(betDto, username);
        return ResponseEntity.ok("Total rounds bet placed or updated successfully.");
    }

    @GetMapping
    public ResponseEntity<?> getAllBets() {
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
