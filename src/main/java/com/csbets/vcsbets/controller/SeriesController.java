package com.csbets.vcsbets.controller;

import com.csbets.vcsbets.dto.match.SeriesInitDto;
import com.csbets.vcsbets.entity.match.SeriesStatus;
import com.csbets.vcsbets.service.SeriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/series")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/init")
    public ResponseEntity<String> fillMatchData(
            @PathVariable Long id,
            @RequestBody SeriesInitDto dto
    ) {
        seriesService.fillMatchData(id, dto);
        return ResponseEntity.ok("Match initialized successfully.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/status")
    public ResponseEntity<String> changeStatus(
            @PathVariable Long id,
            @RequestParam SeriesStatus status
    ) {
        seriesService.changeSeriesStatus(id, status);
        return ResponseEntity.ok("Match status updated to " + status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMatch(@PathVariable Long id) {
        return ResponseEntity.ok(seriesService.getSeriesDto(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllMatches() {
        return ResponseEntity.ok(seriesService.getAllSeries());
    }

}
