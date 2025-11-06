package com.csbets.vcsbets.controller;

import com.csbets.vcsbets.dto.match.MatchDto;
import com.csbets.vcsbets.dto.match.SeriesDto;
import com.csbets.vcsbets.dto.match.SeriesInitDto;
import com.csbets.vcsbets.entity.match.SeriesStatus;
import com.csbets.vcsbets.service.SeriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/series")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/init")
    public ResponseEntity<Void> fillMatchData(
            @PathVariable Long id,
            @RequestBody SeriesInitDto dto
    ) {
        seriesService.fillMatchData(id, dto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(
            @PathVariable Long id,
            @RequestParam SeriesStatus status
    ) {
        seriesService.changeSeriesStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public SeriesDto getMatch(@PathVariable Long id) {
        return seriesService.getSeriesDto(id);
    }

    @GetMapping
    public List<SeriesDto> getAllMatches() {
        return seriesService.getAllSeries();
    }

}
