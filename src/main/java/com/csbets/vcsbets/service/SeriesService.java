package com.csbets.vcsbets.service;

import com.csbets.vcsbets.dto.match.SeriesDto;
import com.csbets.vcsbets.dto.match.SeriesInitDto;
import com.csbets.vcsbets.entity.match.Series;
import com.csbets.vcsbets.entity.match.SeriesFormat;
import com.csbets.vcsbets.entity.match.SeriesResult;
import com.csbets.vcsbets.entity.match.SeriesStatus;
import com.csbets.vcsbets.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final BetsService betsService;

    public void fillMatchData(Long id, SeriesInitDto seriesInitDto) {
        Series series = seriesRepository.findById(id).orElse(null);
        assert series != null;
        series.setTeam1Players(seriesInitDto.team1Players());
        series.setTeam2Players(seriesInitDto.team2Players());
        log.info("Set match data: id {}, {}, {}",
                id, series.getTeam1Name(), series.getTeam2Name());
        seriesRepository.save(series);
    }

    public void changeSeriesStatus(Long id, SeriesStatus seriesStatus) {
        Series series = seriesRepository.findById(id).orElse(null);
        assert series != null;
        series.setStatus(seriesStatus);
        log.info("Match {} status set to: {}", id, series.getStatus());
        seriesRepository.save(series);
    }

    public void changeSeriesResult(Long id, boolean team1won) {
        Series series = seriesRepository.findById(id).orElse(null);
        assert series != null;
        if(team1won) {
            series.setTeam1Score((short) (series.getTeam1Score() + 1));
        } else {
            series.setTeam2Score((short) (series.getTeam2Score() + 1));
        }
        if(series.getSeriesFormat().equals(SeriesFormat.BO3)) {
            if(series.getTeam1Score() == 2 || series.getTeam2Score() == 2) {
                finalizeSeries(series);
            }
        } else if(series.getSeriesFormat().equals(SeriesFormat.BO1)) {
            finalizeSeries(series);
        }
        seriesRepository.save(series);
    }

    private void finalizeSeries(Series series) {
        series.setSeriesResult(series.getTeam1Score() > series.getTeam2Score() ? SeriesResult.TEAM1_WON : SeriesResult.TEAM2_WON);
        log.info("Series {} result set to: {}", series.getId(), series.getSeriesResult());
        changeSeriesStatus(series.getId(), SeriesStatus.ENDED);
        betsService.withdrawalPlacedBets(series.getId());
    }

    public Series getSeries(Long matchId) {
        return seriesRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Матч не найден"));
    }

    public SeriesDto getSeriesDto(Long matchId) {
        return convertToSeriesDTO(seriesRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Матч не найден")));
    }

    public List<SeriesDto> getAllSeries() {
        return seriesRepository.findAll().stream()
                .map(this::convertToSeriesDTO)
                .toList();
    }

    private SeriesDto convertToSeriesDTO(Series series) {
        return new SeriesDto(
                series.getId(),
                series.getTeam1Name(),
                series.getTeam2Name(),
                series.getTeam1Players(),
                series.getTeam2Players(),
                series.getTeam1Score(),
                series.getTeam2Score(),
                series.getStatus(),
                series.getSeriesResult(),
                series.getSeriesFormat()
        );
    }


}
