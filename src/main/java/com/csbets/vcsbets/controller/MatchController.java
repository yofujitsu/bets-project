package com.csbets.vcsbets.controller;

import com.csbets.vcsbets.dto.match.MatchInitDto;
import com.csbets.vcsbets.entity.match.MatchStatus;
import com.csbets.vcsbets.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping("/{id}/init")
    public ResponseEntity<String> fillMatchData(
            @PathVariable Long id,
            @RequestBody MatchInitDto dto
    ) {
        matchService.fillMatchData(id, dto);
        return ResponseEntity.ok("Match initialized successfully.");
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> changeStatus(
            @PathVariable Long id,
            @RequestParam MatchStatus status
    ) {
        matchService.changeMatchStatus(id, status);
        return ResponseEntity.ok("Match status updated to " + status);
    }

    @PutMapping("/{id}/result")
    public ResponseEntity<String> changeResult(
            @PathVariable Long id,
            @RequestParam short team1Score,
            @RequestParam short team2Score
    ) {
        matchService.changeMatchResult(id, team1Score, team2Score);
        return ResponseEntity.ok("Match result updated successfully.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMatch(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.getMatchDto(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllMatches() {
        return ResponseEntity.ok(matchService.getAllMatches());
    }

}
