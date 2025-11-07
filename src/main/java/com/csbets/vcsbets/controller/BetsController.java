package com.csbets.vcsbets.controller;

import com.csbets.vcsbets.dto.bet.BetDto;
import com.csbets.vcsbets.dto.bet.MatchOutcomeBetPlaceDto;
import com.csbets.vcsbets.dto.bet.TotalRoundsBetPlaceDto;
import com.csbets.vcsbets.service.BetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bets")
@RequiredArgsConstructor
public class BetsController {

    private final BetsService betsService;

    @PostMapping("/series-outcome")
    public ResponseEntity<Void> placeMatchOutcomeBet(
            @RequestBody MatchOutcomeBetPlaceDto betDto,
            @RequestParam String username
    ) {
        betsService.placeMatchOutcomeBet(betDto, username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/total")
    public ResponseEntity<Void> placeTotalRoundsBet(
            @RequestBody TotalRoundsBetPlaceDto betDto,
            @RequestParam String username
    ) {
        betsService.placeTotalRoundsBet(betDto, username);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<BetDto> getAllBets() {
        return betsService.getAllBets();
    }

    @GetMapping("/match/{matchId}")
    public List<BetDto> getBetsById(@PathVariable Long matchId) {
        return betsService.getAllBetsByMatchId(matchId);
    }

    @GetMapping("/{matchId}/for-stream")
    public Integer forStream(@PathVariable Long matchId) {
        return betsService.forStream(matchId);
    }

    @GetMapping("/{username}/bets")
    public List<BetDto> getBets(@PathVariable String username) {
        return betsService.getAllBetsByUser(username);
    }
}
