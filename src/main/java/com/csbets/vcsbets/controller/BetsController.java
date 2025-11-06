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
    public void placeMatchOutcomeBet(
            @RequestBody MatchOutcomeBetPlaceDto betDto,
            @RequestParam String username
    ) {
        betsService.placeMatchOutcomeBet(betDto, username);
    }

    @PostMapping("/total")
    public void placeTotalRoundsBet(
            @RequestBody TotalRoundsBetPlaceDto betDto,
            @RequestParam String username
    ) {
        betsService.placeTotalRoundsBet(betDto, username);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<BetDto> getAllBets() {
        return betsService.getAllBets();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/match/{matchId}")
    public List<BetDto> getBetsById(@PathVariable Long matchId) {
        return betsService.getAllBetsByMatchId(matchId);
    }

    @GetMapping("/{username}/bets")
    public List<BetDto> getBets(@PathVariable String username) {
        return betsService.getAllBetsByUser(username);
    }
}
