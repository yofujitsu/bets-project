package com.csbets.vcsbets.controller;

import com.csbets.vcsbets.dto.bet.MatchOutcomeBetPlaceDto;
import com.csbets.vcsbets.dto.bet.TotalRoundsBetPlaceDto;
import com.csbets.vcsbets.service.BetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllBets() {
        return ResponseEntity.ok(betsService.getAllBets());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/match/{matchId}")
    public ResponseEntity<?> getBetsById(@PathVariable Long matchId) {
        return ResponseEntity.ok(betsService.getAllBetsByMatchId(matchId));
    }

    @GetMapping("/{username}/bets")
    public ResponseEntity<?> getBets(@PathVariable String username) {
        return ResponseEntity.ok(betsService.getAllBetsByUser(username));
    }
}
